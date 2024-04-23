import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Visitors from './visitors';
import VisitorsDetail from './visitors-detail';
import VisitorsUpdate from './visitors-update';
import VisitorsDeleteDialog from './visitors-delete-dialog';

const VisitorsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Visitors />} />
    <Route path="new" element={<VisitorsUpdate />} />
    <Route path=":id">
      <Route index element={<VisitorsDetail />} />
      <Route path="edit" element={<VisitorsUpdate />} />
      <Route path="delete" element={<VisitorsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VisitorsRoutes;
