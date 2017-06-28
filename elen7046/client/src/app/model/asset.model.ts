
import { IAsset } from  "../interface/asset.interface";

export class Asset implements IAsset {
    latitude: number;
	longitude: number;
	publishedtime: string;
	deviceid:string;
	label: string;
	title: string;
	draggable: boolean;
}