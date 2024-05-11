import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Activities from './activities';
import ActivitiesDetail from './activities-detail';
import ActivitiesUpdate from './activities-update';
import ActivitiesDeleteDialog from './activities-delete-dialog';

const ActivitiesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Activities />} />
    <Route path="new" element={<ActivitiesUpdate />} />
    <Route path=":id">
      <Route index element={<ActivitiesDetail />} />
      <Route path="edit" element={<ActivitiesUpdate />} />
      <Route path="delete" element={<ActivitiesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ActivitiesRoutes;
