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

describe('Activities e2e test', () => {
  const activitiesPageUrl = '/activities';
  const activitiesPageUrlPattern = new RegExp('/activities(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const activitiesSample = {};

  let activities;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/activities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/activities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/activities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (activities) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/activities/${activities.id}`,
      }).then(() => {
        activities = undefined;
      });
    }
  });

  it('Activities menu should load Activities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('activities');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Activities').should('exist');
    cy.url().should('match', activitiesPageUrlPattern);
  });

  describe('Activities page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(activitiesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Activities page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/activities/new$'));
        cy.getEntityCreateUpdateHeading('Activities');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activitiesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/activities',
          body: activitiesSample,
        }).then(({ body }) => {
          activities = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/activities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [activities],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(activitiesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Activities page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('activities');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activitiesPageUrlPattern);
      });

      it('edit button click should load edit Activities page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Activities');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activitiesPageUrlPattern);
      });

      it('edit button click should load edit Activities page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Activities');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activitiesPageUrlPattern);
      });

      it('last delete button click should delete instance of Activities', () => {
        cy.intercept('GET', '/api/activities/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('activities').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activitiesPageUrlPattern);

        activities = undefined;
      });
    });
  });

  describe('new Activities page', () => {
    beforeEach(() => {
      cy.visit(`${activitiesPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Activities');
    });

    it('should create an instance of Activities', () => {
      cy.get(`[data-cy="total_activities"]`).type('13079');
      cy.get(`[data-cy="total_activities"]`).should('have.value', '13079');

      cy.get(`[data-cy="total_participants"]`).type('996');
      cy.get(`[data-cy="total_participants"]`).should('have.value', '996');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        activities = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', activitiesPageUrlPattern);
    });
  });
});
