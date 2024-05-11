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

describe('ElectronicServices e2e test', () => {
  const electronicServicesPageUrl = '/electronic-services';
  const electronicServicesPageUrlPattern = new RegExp('/electronic-services(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const electronicServicesSample = { total_pcs_with_internet: 29014 };

  let electronicServices;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/electronic-services+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/electronic-services').as('postEntityRequest');
    cy.intercept('DELETE', '/api/electronic-services/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (electronicServices) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/electronic-services/${electronicServices.id}`,
      }).then(() => {
        electronicServices = undefined;
      });
    }
  });

  it('ElectronicServices menu should load ElectronicServices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('electronic-services');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ElectronicServices').should('exist');
    cy.url().should('match', electronicServicesPageUrlPattern);
  });

  describe('ElectronicServices page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(electronicServicesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ElectronicServices page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/electronic-services/new$'));
        cy.getEntityCreateUpdateHeading('ElectronicServices');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', electronicServicesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/electronic-services',
          body: electronicServicesSample,
        }).then(({ body }) => {
          electronicServices = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/electronic-services+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [electronicServices],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(electronicServicesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ElectronicServices page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('electronicServices');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', electronicServicesPageUrlPattern);
      });

      it('edit button click should load edit ElectronicServices page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ElectronicServices');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', electronicServicesPageUrlPattern);
      });

      it('edit button click should load edit ElectronicServices page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ElectronicServices');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', electronicServicesPageUrlPattern);
      });

      it('last delete button click should delete instance of ElectronicServices', () => {
        cy.intercept('GET', '/api/electronic-services/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('electronicServices').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', electronicServicesPageUrlPattern);

        electronicServices = undefined;
      });
    });
  });

  describe('new ElectronicServices page', () => {
    beforeEach(() => {
      cy.visit(`${electronicServicesPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ElectronicServices');
    });

    it('should create an instance of ElectronicServices', () => {
      cy.get(`[data-cy="total_pcs_with_internet"]`).type('17350');
      cy.get(`[data-cy="total_pcs_with_internet"]`).should('have.value', '17350');

      cy.get(`[data-cy="pcs_with_internet_for_clients_only"]`).type('622');
      cy.get(`[data-cy="pcs_with_internet_for_clients_only"]`).should('have.value', '622');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        electronicServices = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', electronicServicesPageUrlPattern);
    });
  });
});
