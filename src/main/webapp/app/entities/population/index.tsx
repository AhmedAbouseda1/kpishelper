import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Population from './population';
import PopulationDetail from './population-detail';
import PopulationUpdate from './population-update';
import PopulationDeleteDialog from './population-delete-dialog';

const PopulationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Population />} />
    <Route path="new" element={<PopulationUpdate />} />
    <Route path=":id">
      <Route index element={<PopulationDetail />} />
      <Route path="edit" element={<PopulationUpdate />} />
      <Route path="delete" element={<PopulationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PopulationRoutes;
