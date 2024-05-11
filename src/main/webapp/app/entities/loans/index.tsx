import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Loans from './loans';
import LoansDetail from './loans-detail';
import LoansUpdate from './loans-update';
import LoansDeleteDialog from './loans-delete-dialog';

const LoansRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Loans />} />
    <Route path="new" element={<LoansUpdate />} />
    <Route path=":id">
      <Route index element={<LoansDetail />} />
      <Route path="edit" element={<LoansUpdate />} />
      <Route path="delete" element={<LoansDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LoansRoutes;
