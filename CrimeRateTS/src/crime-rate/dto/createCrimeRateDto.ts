import { IsDateString, IsNotEmpty, IsNumber } from 'class-validator';

export class CreateCrimeRateDto {
  @IsNumber()
  @IsNotEmpty()
  cityId: number;

  @IsNumber()
  @IsNotEmpty()
  crimeIndex: number;

  @IsNumber()
  @IsNotEmpty()
  safetyIndex: number;

  @IsDateString()
  @IsNotEmpty()
  concludedAt: Date;
}