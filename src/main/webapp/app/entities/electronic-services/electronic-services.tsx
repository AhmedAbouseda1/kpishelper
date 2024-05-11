import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './electronic-services.reducer';

export const ElectronicServices = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const electronicServicesList = useAppSelector(state => state.electronicServices.entities);
  const loading = useAppSelector(state => state.electronicServices.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="electronic-services-heading" data-cy="ElectronicServicesHeading">
        <Translate contentKey="kpishelperApp.electronicServices.home.title">Electronic Services</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="kpishelperApp.electronicServices.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/electronic-services/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="kpishelperApp.electronicServices.home.createLabel">Create new Electronic Services</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {electronicServicesList && electronicServicesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="kpishelperApp.electronicServices.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('recorded_date')}>
                  <Translate contentKey="kpishelperApp.electronicServices.recorded_date">Recorded Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('recorded_date')} />
                </th>
                <th className="hand" onClick={sort('total_pcs_with_internet')}>
                  <Translate contentKey="kpishelperApp.electronicServices.total_pcs_with_internet">Total Pcs With Internet</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('total_pcs_with_internet')} />
                </th>
                <th className="hand" onClick={sort('pcs_with_internet_for_clients_only')}>
                  <Translate contentKey="kpishelperApp.electronicServices.pcs_with_internet_for_clients_only">
                    Pcs With Internet For Clients Only
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pcs_with_internet_for_clients_only')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {electronicServicesList.map((electronicServices, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/electronic-services/${electronicServices.id}`} color="link" size="sm">
                      {electronicServices.id}
                    </Button>
                  </td>
                  <td>
                    {electronicServices.recorded_date ? (
                      <TextFormat type="date" value={electronicServices.recorded_date} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{electronicServices.total_pcs_with_internet}</td>
                  <td>{electronicServices.pcs_with_internet_for_clients_only}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/electronic-services/${electronicServices.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/electronic-services/${electronicServices.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/electronic-services/${electronicServices.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="kpishelperApp.electronicServices.home.notFound">No Electronic Services found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ElectronicServices;
