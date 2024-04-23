import dayjs from 'dayjs';

export interface IVisitors {
  id?: number;
  total_visitors?: number | null;
  website_visitors?: number | null;
  recorded_date?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IVisitors> = {};
