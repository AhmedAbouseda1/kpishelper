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

import { getEntities } from './loans.reducer';

export const Loans = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const loansList = useAppSelector(state => state.loans.entities);
  const loading = useAppSelector(state => state.loans.loading);

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
      <h2 id="loans-heading" data-cy="LoansHeading">
        <Translate contentKey="kpishelperApp.loans.home.title">Loans</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="kpishelperApp.loans.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/loans/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="kpishelperApp.loans.home.createLabel">Create new Loans</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {loansList && loansList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="kpishelperApp.loans.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('recorded_date')}>
                  <Translate contentKey="kpishelperApp.loans.recorded_date">Recorded Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('recorded_date')} />
                </th>
                <th className="hand" onClick={sort('total_items_borrowed')}>
                  <Translate contentKey="kpishelperApp.loans.total_items_borrowed">Total Items Borrowed</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('total_items_borrowed')} />
                </th>
                <th className="hand" onClick={sort('turnover_rate')}>
                  <Translate contentKey="kpishelperApp.loans.turnover_rate">Turnover Rate</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('turnover_rate')} />
                </th>
                <th className="hand" onClick={sort('media_borrowed_at_least_once_percentage')}>
                  <Translate contentKey="kpishelperApp.loans.media_borrowed_at_least_once_percentage">
                    Media Borrowed At Least Once Percentage
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('media_borrowed_at_least_once_percentage')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {loansList.map((loans, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/loans/${loans.id}`} color="link" size="sm">
                      {loans.id}
                    </Button>
                  </td>
                  <td>
                    {loans.recorded_date ? <TextFormat type="date" value={loans.recorded_date} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{loans.total_items_borrowed}</td>
                  <td>{loans.turnover_rate}</td>
                  <td>{loans.media_borrowed_at_least_once_percentage}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/loans/${loans.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/loans/${loans.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/loans/${loans.id}/delete`)}
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
              <Translate contentKey="kpishelperApp.loans.home.notFound">No Loans found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Loans;
