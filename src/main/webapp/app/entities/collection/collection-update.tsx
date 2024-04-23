import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILibrary } from 'app/shared/model/library.model';
import { getEntities as getLibraries } from 'app/entities/library/library.reducer';
import { ICollection } from 'app/shared/model/collection.model';
import { getEntity, updateEntity, createEntity, reset } from './collection.reducer';

export const CollectionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const libraries = useAppSelector(state => state.library.entities);
  const collectionEntity = useAppSelector(state => state.collection.entity);
  const loading = useAppSelector(state => state.collection.loading);
  const updating = useAppSelector(state => state.collection.updating);
  const updateSuccess = useAppSelector(state => state.collection.updateSuccess);

  const handleClose = () => {
    navigate('/collection');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLibraries({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.collection_size !== undefined && typeof values.collection_size !== 'number') {
      values.collection_size = Number(values.collection_size);
    }
    if (values.number_of_titles !== undefined && typeof values.number_of_titles !== 'number') {
      values.number_of_titles = Number(values.number_of_titles);
    }
    if (values.stock_for_public_usage !== undefined && typeof values.stock_for_public_usage !== 'number') {
      values.stock_for_public_usage = Number(values.stock_for_public_usage);
    }
    if (values.titles_availability_for_population !== undefined && typeof values.titles_availability_for_population !== 'number') {
      values.titles_availability_for_population = Number(values.titles_availability_for_population);
    }
    if (values.titles_availability_for_active_members !== undefined && typeof values.titles_availability_for_active_members !== 'number') {
      values.titles_availability_for_active_members = Number(values.titles_availability_for_active_members);
    }

    const entity = {
      ...collectionEntity,
      ...values,
      library: libraries.find(it => it.id.toString() === values.library?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...collectionEntity,
          library: collectionEntity?.library?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="kpishelperApp.collection.home.createOrEditLabel" data-cy="CollectionCreateUpdateHeading">
            <Translate contentKey="kpishelperApp.collection.home.createOrEditLabel">Create or edit a Collection</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="collection-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('kpishelperApp.collection.date_recorded')}
                id="collection-date_recorded"
                name="date_recorded"
                data-cy="date_recorded"
                type="date"
              />
              <ValidatedField
                label={translate('kpishelperApp.collection.collection_size')}
                id="collection-collection_size"
                name="collection_size"
                data-cy="collection_size"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('kpishelperApp.collection.number_of_titles')}
                id="collection-number_of_titles"
                name="number_of_titles"
                data-cy="number_of_titles"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('kpishelperApp.collection.stock_for_public_usage')}
                id="collection-stock_for_public_usage"
                name="stock_for_public_usage"
                data-cy="stock_for_public_usage"
                type="text"
              />
              <ValidatedField
                label={translate('kpishelperApp.collection.titles_availability_for_population')}
                id="collection-titles_availability_for_population"
                name="titles_availability_for_population"
                data-cy="titles_availability_for_population"
                type="text"
              />
              <ValidatedField
                label={translate('kpishelperApp.collection.titles_availability_for_active_members')}
                id="collection-titles_availability_for_active_members"
                name="titles_availability_for_active_members"
                data-cy="titles_availability_for_active_members"
                type="text"
              />
              <ValidatedField
                id="collection-library"
                name="library"
                data-cy="library"
                label={translate('kpishelperApp.collection.library')}
                type="select"
              >
                <option value="" key="0" />
                {libraries
                  ? libraries.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/collection" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CollectionUpdate;
