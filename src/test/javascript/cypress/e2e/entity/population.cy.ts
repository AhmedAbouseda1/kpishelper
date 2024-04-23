import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Population e2e test', () => {
  const populationPageUrl = '/population';
  const populationPageUrlPattern = new RegExp('/population(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const populationSample = { date_recorded: '2024-04-22', population: 1019 };

  let population;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/populations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/populations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/populations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (population) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/populations/${population.id}`,
      }).then(() => {
        population = undefined;
      });
    }
  });

  it('Populations menu should load Populations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('population');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Population').should('exist');
    cy.url().should('match', populationPageUrlPattern);
  });

  describe('Population page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(populationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Population page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/population/new$'));
        cy.getEntityCreateUpdateHeading('Population');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', populationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/populations',
          body: populationSample,
        }).then(({ body }) => {
          population = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/populations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/populations?page=0&size=20>; rel="last",<http://localhost/api/populations?page=0&size=20>; rel="first"',
              },
              body: [population],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(populationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Population page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('population');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', populationPageUrlPattern);
      });

      it('edit button click should load edit Population page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Population');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', populationPageUrlPattern);
      });

      it('edit button click should load edit Population page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Population');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', populationPageUrlPattern);
      });

      it('last delete button click should delete instance of Population', () => {
        cy.intercept('GET', '/api/populations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('population').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', populationPageUrlPattern);

        population = undefined;
      });
    });
  });

  describe('new Population page', () => {
    beforeEach(() => {
      cy.visit(`${populationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Population');
    });

    it('should create an instance of Population', () => {
      cy.get(`[data-cy="date_recorded"]`).type('2024-04-22');
      cy.get(`[data-cy="date_recorded"]`).blur();
      cy.get(`[data-cy="date_recorded"]`).should('have.value', '2024-04-22');

      cy.get(`[data-cy="population"]`).type('8363');
      cy.get(`[data-cy="population"]`).should('have.value', '8363');

      cy.get(`[data-cy="active_members"]`).type('26964');
      cy.get(`[data-cy="active_members"]`).should('have.value', '26964');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        population = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', populationPageUrlPattern);
    });
  });
});
