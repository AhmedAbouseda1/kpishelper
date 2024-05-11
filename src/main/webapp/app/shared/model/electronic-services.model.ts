import dayjs from 'dayjs';

export interface IElectronicServices {
  id?: number;
  recorded_date?: dayjs.Dayjs;
  total_pcs_with_internet?: number;
  pcs_with_internet_for_clients_only?: number | null;
}

export const defaultValue: Readonly<IElectronicServices> = {};
