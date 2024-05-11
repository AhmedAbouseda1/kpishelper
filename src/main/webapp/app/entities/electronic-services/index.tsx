import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ElectronicServices from './electronic-services';
import ElectronicServicesDetail from './electronic-services-detail';
import ElectronicServicesUpdate from './electronic-services-update';
import ElectronicServicesDeleteDialog from './electronic-services-delete-dialog';

const ElectronicServicesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ElectronicServices />} />
    <Route path="new" element={<ElectronicServicesUpdate />} />
    <Route path=":id">
      <Route index element={<ElectronicServicesDetail />} />
      <Route path="edit" element={<ElectronicServicesUpdate />} />
      <Route path="delete" element={<ElectronicServicesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ElectronicServicesRoutes;
