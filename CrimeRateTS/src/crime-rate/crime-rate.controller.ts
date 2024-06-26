import { Body, Controller, Get, Post, Query, UsePipes, ValidationPipe } from "@nestjs/common";
import { CreateCrimeRateDto } from "./dto/createCrimeRateDto";
import { CrimeRateService } from "./crime-rate.service";

@Controller('api/crime-rate')
export class CrimeRateController {
  constructor(private readonly crimeRateService: CrimeRateService) {}

  @Post('/')
  @UsePipes(new ValidationPipe())
  public async createCrimeRate(@Body() createCrimeRateDto: CreateCrimeRateDto) {
    const createdCrimeRate = await this.crimeRateService.createCrimeRate(createCrimeRateDto);
    return { id: createdCrimeRate._id };
  }

  @Get('/')
  public async getCrimeRates(
    @Query('cityId') cityId: number,
    @Query('size') size: number,
    @Query('from') from: number,
  ) {
    return await this.crimeRateService.getCrimeRates(cityId, size, from);
  }

  @Post('/_counts')
  @UsePipes(new ValidationPipe())
  public async getCrimeRatesCounts(@Body('cityIds') cityIds: number[]): Promise<{ [key: number]: number }> {
    return await this.crimeRateService.getCrimeRatesCount(cityIds);
  }
}