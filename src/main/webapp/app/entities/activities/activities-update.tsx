import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IActivities } from 'app/shared/model/activities.model';
import { getEntity, updateEntity, createEntity, reset } from './activities.reducer';

export const ActivitiesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const activitiesEntity = useAppSelector(state => state.activities.entity);
  const loading = useAppSelector(state => state.activities.loading);
  const updating = useAppSelector(state => state.activities.updating);
  const updateSuccess = useAppSelector(state => state.activities.updateSuccess);

  const handleClose = () => {
    navigate('/activities');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.total_activities !== undefined && typeof values.total_activities !== 'number') {
      values.total_activities = Number(values.total_activities);
    }
    if (values.total_participants !== undefined && typeof values.total_participants !== 'number') {
      values.total_participants = Number(values.total_participants);
    }

    const entity = {
      ...activitiesEntity,
      ...values,
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
          ...activitiesEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="kpishelperApp.activities.home.createOrEditLabel" data-cy="ActivitiesCreateUpdateHeading">
            <Translate contentKey="kpishelperApp.activities.home.createOrEditLabel">Create or edit a Activities</Translate>
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
                  id="activities-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('kpishelperApp.activities.recorded_date')}
                id="activities-recorded_date"
                name="recorded_date"
                data-cy="recorded_date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('kpishelperApp.activities.total_activities')}
                id="activities-total_activities"
                name="total_activities"
                data-cy="total_activities"
                type="text"
              />
              <ValidatedField
                label={translate('kpishelperApp.activities.total_participants')}
                id="activities-total_participants"
                name="total_participants"
                data-cy="total_participants"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/activities" replace color="info">
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

export default ActivitiesUpdate;
