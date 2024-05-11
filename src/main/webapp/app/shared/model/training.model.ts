import dayjs from 'dayjs';

export interface ITraining {
  id?: number;
  recorded_date?: dayjs.Dayjs;
  total_courses?: number | null;
  total_participants?: number | null;
}

export const defaultValue: Readonly<ITraining> = {};
