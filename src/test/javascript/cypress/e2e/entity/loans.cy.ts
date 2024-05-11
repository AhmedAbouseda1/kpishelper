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

describe('Loans e2e test', () => {
  const loansPageUrl = '/loans';
  const loansPageUrlPattern = new RegExp('/loans(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const loansSample = {};

  let loans;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/loans+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/loans').as('postEntityRequest');
    cy.intercept('DELETE', '/api/loans/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (loans) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/loans/${loans.id}`,
      }).then(() => {
        loans = undefined;
      });
    }
  });

  it('Loans menu should load Loans page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('loans');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Loans').should('exist');
    cy.url().should('match', loansPageUrlPattern);
  });

  describe('Loans page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(loansPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Loans page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/loans/new$'));
        cy.getEntityCreateUpdateHeading('Loans');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', loansPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/loans',
          body: loansSample,
        }).then(({ body }) => {
          loans = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/loans+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [loans],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(loansPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Loans page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('loans');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', loansPageUrlPattern);
      });

      it('edit button click should load edit Loans page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Loans');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', loansPageUrlPattern);
      });

      it('edit button click should load edit Loans page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Loans');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', loansPageUrlPattern);
      });

      it('last delete button click should delete instance of Loans', () => {
        cy.intercept('GET', '/api/loans/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('loans').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', loansPageUrlPattern);

        loans = undefined;
      });
    });
  });

  describe('new Loans page', () => {
    beforeEach(() => {
      cy.visit(`${loansPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Loans');
    });

    it('should create an instance of Loans', () => {
      cy.get(`[data-cy="total_items_borrowed"]`).type('26246');
      cy.get(`[data-cy="total_items_borrowed"]`).should('have.value', '26246');

      cy.get(`[data-cy="turnover_rate"]`).type('13283');
      cy.get(`[data-cy="turnover_rate"]`).should('have.value', '13283');

      cy.get(`[data-cy="media_borrowed_at_least_once_percentage"]`).type('32439');
      cy.get(`[data-cy="media_borrowed_at_least_once_percentage"]`).should('have.value', '32439');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        loans = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', loansPageUrlPattern);
    });
  });
});
