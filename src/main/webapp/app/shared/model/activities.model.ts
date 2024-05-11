import dayjs from 'dayjs';

export interface IActivities {
  id?: number;
  recorded_date?: dayjs.Dayjs;
  total_activities?: number | null;
  total_participants?: number | null;
}

export const defaultValue: Readonly<IActivities> = {};
