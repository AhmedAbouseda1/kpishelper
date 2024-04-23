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

import { getEntities } from './collection.reducer';

export const Collection = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const collectionList = useAppSelector(state => state.collection.entities);
  const loading = useAppSelector(state => state.collection.loading);

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
      <h2 id="collection-heading" data-cy="CollectionHeading">
        <Translate contentKey="kpishelperApp.collection.home.title">Collections</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="kpishelperApp.collection.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/collection/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="kpishelperApp.collection.home.createLabel">Create new Collection</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {collectionList && collectionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="kpishelperApp.collection.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('date_recorded')}>
                  <Translate contentKey="kpishelperApp.collection.date_recorded">Date Recorded</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('date_recorded')} />
                </th>
                <th className="hand" onClick={sort('collection_size')}>
                  <Translate contentKey="kpishelperApp.collection.collection_size">Collection Size</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('collection_size')} />
                </th>
                <th className="hand" onClick={sort('number_of_titles')}>
                  <Translate contentKey="kpishelperApp.collection.number_of_titles">Number Of Titles</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('number_of_titles')} />
                </th>
                <th className="hand" onClick={sort('stock_for_public_usage')}>
                  <Translate contentKey="kpishelperApp.collection.stock_for_public_usage">Stock For Public Usage</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stock_for_public_usage')} />
                </th>
                <th className="hand" onClick={sort('titles_availability_for_population')}>
                  <Translate contentKey="kpishelperApp.collection.titles_availability_for_population">
                    Titles Availability For Population
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('titles_availability_for_population')} />
                </th>
                <th className="hand" onClick={sort('titles_availability_for_active_members')}>
                  <Translate contentKey="kpishelperApp.collection.titles_availability_for_active_members">
                    Titles Availability For Active Members
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('titles_availability_for_active_members')} />
                </th>
                <th>
                  <Translate contentKey="kpishelperApp.collection.library">Library</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {collectionList.map((collection, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/collection/${collection.id}`} color="link" size="sm">
                      {collection.id}
                    </Button>
                  </td>
                  <td>
                    {collection.date_recorded ? (
                      <TextFormat type="date" value={collection.date_recorded} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{collection.collection_size}</td>
                  <td>{collection.number_of_titles}</td>
                  <td>{collection.stock_for_public_usage}</td>
                  <td>{collection.titles_availability_for_population}</td>
                  <td>{collection.titles_availability_for_active_members}</td>
                  <td>{collection.library ? <Link to={`/library/${collection.library.id}`}>{collection.library.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/collection/${collection.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/collection/${collection.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/collection/${collection.id}/delete`)}
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
              <Translate contentKey="kpishelperApp.collection.home.notFound">No Collections found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Collection;
