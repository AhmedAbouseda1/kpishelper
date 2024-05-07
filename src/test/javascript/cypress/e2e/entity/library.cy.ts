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

describe('Library e2e test', () => {
  const libraryPageUrl = '/library';
  const libraryPageUrlPattern = new RegExp('/library(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const librarySample = { name: 'against' };

  let library;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/libraries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/libraries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/libraries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (library) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/libraries/${library.id}`,
      }).then(() => {
        library = undefined;
      });
    }
  });

  it('Libraries menu should load Libraries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('library');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Library').should('exist');
    cy.url().should('match', libraryPageUrlPattern);
  });

  describe('Library page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(libraryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Library page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/library/new$'));
        cy.getEntityCreateUpdateHeading('Library');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', libraryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/libraries',
          body: librarySample,
        }).then(({ body }) => {
          library = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/libraries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [library],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(libraryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Library page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('library');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', libraryPageUrlPattern);
      });

      it('edit button click should load edit Library page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Library');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', libraryPageUrlPattern);
      });

      it('edit button click should load edit Library page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Library');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', libraryPageUrlPattern);
      });

      it('last delete button click should delete instance of Library', () => {
        cy.intercept('GET', '/api/libraries/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('library').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', libraryPageUrlPattern);

        library = undefined;
      });
    });
  });

  describe('new Library page', () => {
    beforeEach(() => {
      cy.visit(`${libraryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Library');
    });

    it('should create an instance of Library', () => {
      cy.get(`[data-cy="name"]`).type('preface');
      cy.get(`[data-cy="name"]`).should('have.value', 'preface');

      cy.get(`[data-cy="location"]`).type('likewise pfft');
      cy.get(`[data-cy="location"]`).should('have.value', 'likewise pfft');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        library = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', libraryPageUrlPattern);
    });
  });
});
