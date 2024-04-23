import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/library">
        <Translate contentKey="global.menu.entities.library" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/collection">
        <Translate contentKey="global.menu.entities.collection" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/population">
        <Translate contentKey="global.menu.entities.population" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/visitors">
        <Translate contentKey="global.menu.entities.visitors" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
