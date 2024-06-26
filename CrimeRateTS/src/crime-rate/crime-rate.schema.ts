import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { HydratedDocument } from 'mongoose';

export type CrimeRateDocument = HydratedDocument<CrimeRate>;

@Schema()
export class CrimeRate {
  @Prop({ required: true })
  cityId: number;

  @Prop({ required: true })
  crimeIndex: number;

  @Prop({ required: true })
  safetyIndex: number;

  @Prop({ required: true })
  concludedAt: Date;
}

export const CrimeRateSchema = SchemaFactory.createForClass(CrimeRate);

CrimeRateSchema.index({ cityId: 1, crimeIndex: 1, safetyIndex: 1, concludedAt: 1 }, { unique: true });