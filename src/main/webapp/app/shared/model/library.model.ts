export interface ILibrary {
  id?: number;
  name?: string;
  location?: string | null;
}

export const defaultValue: Readonly<ILibrary> = {};
