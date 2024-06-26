import { Module } from '@nestjs/common';
import { CrimeRateController } from './crime-rate.controller';
import { CrimeRateService } from './crime-rate.service';
import { MongooseModule } from '@nestjs/mongoose';
import { CrimeRate, CrimeRateSchema } from './crime-rate.schema';
import { JavaApiModule } from '../shared/java-service/java-api.module';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: CrimeRate.name, schema: CrimeRateSchema },
    ]),
    JavaApiModule,
  ],
  controllers: [CrimeRateController],
  providers: [CrimeRateService],
})
export class CrimeRateModule {
}