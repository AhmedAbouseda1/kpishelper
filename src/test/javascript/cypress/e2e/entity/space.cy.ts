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

describe('Space e2e test', () => {
  const spacePageUrl = '/space';
  const spacePageUrlPattern = new RegExp('/space(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const spaceSample = {};

  let space;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/spaces+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/spaces').as('postEntityRequest');
    cy.intercept('DELETE', '/api/spaces/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (space) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/spaces/${space.id}`,
      }).then(() => {
        space = undefined;
      });
    }
  });

  it('Spaces menu should load Spaces page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('space');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Space').should('exist');
    cy.url().should('match', spacePageUrlPattern);
  });

  describe('Space page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(spacePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Space page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/space/new$'));
        cy.getEntityCreateUpdateHeading('Space');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', spacePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/spaces',
          body: spaceSample,
        }).then(({ body }) => {
          space = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/spaces+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [space],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(spacePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Space page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('space');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', spacePageUrlPattern);
      });

      it('edit button click should load edit Space page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Space');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', spacePageUrlPattern);
      });

      it('edit button click should load edit Space page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Space');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', spacePageUrlPattern);
      });

      it('last delete button click should delete instance of Space', () => {
        cy.intercept('GET', '/api/spaces/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('space').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', spacePageUrlPattern);

        space = undefined;
      });
    });
  });

  describe('new Space page', () => {
    beforeEach(() => {
      cy.visit(`${spacePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Space');
    });

    it('should create an instance of Space', () => {
      cy.get(`[data-cy="square_meters_available"]`).type('24961');
      cy.get(`[data-cy="square_meters_available"]`).should('have.value', '24961');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        space = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', spacePageUrlPattern);
    });
  });
});
