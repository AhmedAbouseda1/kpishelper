import dayjs from 'dayjs';

export interface IStaff {
  id?: number;
  recorded_date?: dayjs.Dayjs;
  number_of_staff?: number;
}

export const defaultValue: Readonly<IStaff> = {};
