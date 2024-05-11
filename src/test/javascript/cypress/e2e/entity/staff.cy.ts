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

describe('Staff e2e test', () => {
  const staffPageUrl = '/staff';
  const staffPageUrlPattern = new RegExp('/staff(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const staffSample = { number_of_staff: 20765 };

  let staff;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/staff+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/staff').as('postEntityRequest');
    cy.intercept('DELETE', '/api/staff/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (staff) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/staff/${staff.id}`,
      }).then(() => {
        staff = undefined;
      });
    }
  });

  it('Staff menu should load Staff page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('staff');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Staff').should('exist');
    cy.url().should('match', staffPageUrlPattern);
  });

  describe('Staff page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(staffPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Staff page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/staff/new$'));
        cy.getEntityCreateUpdateHeading('Staff');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', staffPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/staff',
          body: staffSample,
        }).then(({ body }) => {
          staff = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/staff+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [staff],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(staffPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Staff page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('staff');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', staffPageUrlPattern);
      });

      it('edit button click should load edit Staff page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Staff');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', staffPageUrlPattern);
      });

      it('edit button click should load edit Staff page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Staff');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', staffPageUrlPattern);
      });

      it('last delete button click should delete instance of Staff', () => {
        cy.intercept('GET', '/api/staff/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('staff').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', staffPageUrlPattern);

        staff = undefined;
      });
    });
  });

  describe('new Staff page', () => {
    beforeEach(() => {
      cy.visit(`${staffPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Staff');
    });

    it('should create an instance of Staff', () => {
      cy.get(`[data-cy="number_of_staff"]`).type('27883');
      cy.get(`[data-cy="number_of_staff"]`).should('have.value', '27883');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        staff = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', staffPageUrlPattern);
    });
  });
});
