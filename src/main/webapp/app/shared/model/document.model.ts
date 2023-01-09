import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IDocument {
  id?: number;
  documentContentContentType?: string | null;
  documentContent?: string | null;
  collaboratorList?: string | null;
  viewerList?: string | null;
  documentTitle?: string | null;
  createdDate?: string | null;
  modifiedDate?: string | null;
  locationOfTheDocument?: string | null;
  owner?: IUser | null;
}

export const defaultValue: Readonly<IDocument> = {};
