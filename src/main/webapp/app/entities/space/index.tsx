import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Space from './space';
import SpaceDetail from './space-detail';
import SpaceUpdate from './space-update';
import SpaceDeleteDialog from './space-delete-dialog';

const SpaceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Space />} />
    <Route path="new" element={<SpaceUpdate />} />
    <Route path=":id">
      <Route index element={<SpaceDetail />} />
      <Route path="edit" element={<SpaceUpdate />} />
      <Route path="delete" element={<SpaceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SpaceRoutes;
