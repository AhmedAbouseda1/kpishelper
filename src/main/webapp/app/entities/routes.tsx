import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Library from './library';
import Collection from './collection';
import Population from './population';
import Visitors from './visitors';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="library/*" element={<Library />} />
        <Route path="collection/*" element={<Collection />} />
        <Route path="population/*" element={<Population />} />
        <Route path="visitors/*" element={<Visitors />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
