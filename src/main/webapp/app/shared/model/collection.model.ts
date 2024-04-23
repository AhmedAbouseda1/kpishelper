import dayjs from 'dayjs';
import { ILibrary } from 'app/shared/model/library.model';

export interface ICollection {
  id?: number;
  date_recorded?: dayjs.Dayjs | null;
  collection_size?: number;
  number_of_titles?: number;
  stock_for_public_usage?: number | null;
  titles_availability_for_population?: number | null;
  titles_availability_for_active_members?: number | null;
  library?: ILibrary | null;
}

export const defaultValue: Readonly<ICollection> = {};
