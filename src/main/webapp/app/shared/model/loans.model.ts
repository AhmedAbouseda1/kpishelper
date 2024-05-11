import dayjs from 'dayjs';

export interface ILoans {
  id?: number;
  recorded_date?: dayjs.Dayjs;
  total_items_borrowed?: number | null;
  turnover_rate?: number | null;
  media_borrowed_at_least_once_percentage?: number | null;
}

export const defaultValue: Readonly<ILoans> = {};
