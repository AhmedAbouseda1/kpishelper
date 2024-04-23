import library from 'app/entities/library/library.reducer';
import collection from 'app/entities/collection/collection.reducer';
import population from 'app/entities/population/population.reducer';
import visitors from 'app/entities/visitors/visitors.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  library,
  collection,
  population,
  visitors,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
