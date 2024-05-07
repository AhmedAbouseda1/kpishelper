import library from 'app/entities/library/library.reducer';
import collection from 'app/entities/collection/collection.reducer';
import population from 'app/entities/population/population.reducer';
import visitors from 'app/entities/visitors/visitors.reducer';
import staff from 'app/entities/staff/staff.reducer';
import electronicServices from 'app/entities/electronic-services/electronic-services.reducer';
import space from 'app/entities/space/space.reducer';
import loans from 'app/entities/loans/loans.reducer';
import activities from 'app/entities/activities/activities.reducer';
import training from 'app/entities/training/training.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  library,
  collection,
  population,
  visitors,
  staff,
  electronicServices,
  space,
  loans,
  activities,
  training,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
