import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Library from './library';
import Collection from './collection';
import Population from './population';
import Visitors from './visitors';
import Staff from './staff';
import ElectronicServices from './electronic-services';
import Space from './space';
import Loans from './loans';
import Activities from './activities';
import Training from './training';
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
        <Route path="staff/*" element={<Staff />} />
        <Route path="electronic-services/*" element={<ElectronicServices />} />
        <Route path="space/*" element={<Space />} />
        <Route path="loans/*" element={<Loans />} />
        <Route path="activities/*" element={<Activities />} />
        <Route path="training/*" element={<Training />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
