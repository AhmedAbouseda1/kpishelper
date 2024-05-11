import dayjs from 'dayjs';

export interface ISpace {
  id?: number;
  recorded_date?: dayjs.Dayjs;
  square_meters_available?: number | null;
}

export const defaultValue: Readonly<ISpace> = {};
