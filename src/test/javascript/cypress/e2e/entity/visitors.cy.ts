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

describe('Visitors e2e test', () => {
  const visitorsPageUrl = '/visitors';
  const visitorsPageUrlPattern = new RegExp('/visitors(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const visitorsSample = {};

  let visitors;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/visitors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/visitors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/visitors/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (visitors) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/visitors/${visitors.id}`,
      }).then(() => {
        visitors = undefined;
      });
    }
  });

  it('Visitors menu should load Visitors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('visitors');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Visitors').should('exist');
    cy.url().should('match', visitorsPageUrlPattern);
  });

  describe('Visitors page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(visitorsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Visitors page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/visitors/new$'));
        cy.getEntityCreateUpdateHeading('Visitors');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', visitorsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/visitors',
          body: visitorsSample,
        }).then(({ body }) => {
          visitors = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/visitors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/visitors?page=0&size=20>; rel="last",<http://localhost/api/visitors?page=0&size=20>; rel="first"',
              },
              body: [visitors],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(visitorsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Visitors page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('visitors');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', visitorsPageUrlPattern);
      });

      it('edit button click should load edit Visitors page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Visitors');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', visitorsPageUrlPattern);
      });

      it('edit button click should load edit Visitors page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Visitors');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', visitorsPageUrlPattern);
      });

      it('last delete button click should delete instance of Visitors', () => {
        cy.intercept('GET', '/api/visitors/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('visitors').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', visitorsPageUrlPattern);

        visitors = undefined;
      });
    });
  });

  describe('new Visitors page', () => {
    beforeEach(() => {
      cy.visit(`${visitorsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Visitors');
    });

    it('should create an instance of Visitors', () => {
      cy.get(`[data-cy="total_visitors"]`).type('21508');
      cy.get(`[data-cy="total_visitors"]`).should('have.value', '21508');

      cy.get(`[data-cy="website_visitors"]`).type('28869');
      cy.get(`[data-cy="website_visitors"]`).should('have.value', '28869');

      cy.get(`[data-cy="recorded_date"]`).type('2024-04-22');
      cy.get(`[data-cy="recorded_date"]`).blur();
      cy.get(`[data-cy="recorded_date"]`).should('have.value', '2024-04-22');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        visitors = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', visitorsPageUrlPattern);
    });
  });
});
