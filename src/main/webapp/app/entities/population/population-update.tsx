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
import { IPopulation } from 'app/shared/model/population.model';
import { getEntity, updateEntity, createEntity, reset } from './population.reducer';

export const PopulationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const libraries = useAppSelector(state => state.library.entities);
  const populationEntity = useAppSelector(state => state.population.entity);
  const loading = useAppSelector(state => state.population.loading);
  const updating = useAppSelector(state => state.population.updating);
  const updateSuccess = useAppSelector(state => state.population.updateSuccess);

  const handleClose = () => {
    navigate('/population');
  };

  useEffect(() => {
    if (!isNew) {
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
    if (values.population !== undefined && typeof values.population !== 'number') {
      values.population = Number(values.population);
    }
    if (values.active_members !== undefined && typeof values.active_members !== 'number') {
      values.active_members = Number(values.active_members);
    }

    const entity = {
      ...populationEntity,
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
          ...populationEntity,
          library: populationEntity?.library?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="kpishelperApp.population.home.createOrEditLabel" data-cy="PopulationCreateUpdateHeading">
            <Translate contentKey="kpishelperApp.population.home.createOrEditLabel">Create or edit a Population</Translate>
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
                  id="population-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('kpishelperApp.population.date_recorded')}
                id="population-date_recorded"
                name="date_recorded"
                data-cy="date_recorded"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('kpishelperApp.population.population')}
                id="population-population"
                name="population"
                data-cy="population"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('kpishelperApp.population.active_members')}
                id="population-active_members"
                name="active_members"
                data-cy="active_members"
                type="text"
              />
              <ValidatedField
                id="population-library"
                name="library"
                data-cy="library"
                label={translate('kpishelperApp.population.library')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/population" replace color="info">
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

export default PopulationUpdate;
