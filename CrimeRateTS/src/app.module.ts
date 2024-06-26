import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { MongooseModule } from '@nestjs/mongoose';
import { CrimeRateModule } from './crime-rate/crime-rate.module';
import { JavaApiModule } from './shared/java-service/java-api.module';

@Module({
  imports: [
    MongooseModule.forRoot('mongodb://mongodb:27017/crime-rate'),
    CrimeRateModule,
    JavaApiModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}