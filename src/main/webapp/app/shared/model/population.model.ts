import dayjs from 'dayjs';
import { ILibrary } from 'app/shared/model/library.model';

export interface IPopulation {
  id?: number;
  date_recorded?: dayjs.Dayjs;
  population?: number;
  active_members?: number | null;
  library?: ILibrary | null;
}

export const defaultValue: Readonly<IPopulation> = {};
