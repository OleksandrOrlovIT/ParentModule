import { Test, TestingModule } from '@nestjs/testing';
import { CrimeRateController } from './crime-rate.controller';
import { CrimeRateService } from './crime-rate.service';
import { CreateCrimeRateDto } from './dto/createCrimeRateDto';
import { CrimeRateDocument } from "./crime-rate.schema";

describe('CrimeRateController', () => {
  let controller: CrimeRateController;
  let service: CrimeRateService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [CrimeRateController],
      providers: [
        {
          provide: CrimeRateService,
          useValue: {
            createCrimeRate: jest.fn(),
            getCrimeRates: jest.fn(),
            getCrimeRatesCount: jest.fn(),
          },
        },
      ],
    }).compile();

    controller = module.get<CrimeRateController>(CrimeRateController);
    service = module.get<CrimeRateService>(CrimeRateService);
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });

  describe('createCrimeRate', () => {
    it('should return the created crime rate id', async () => {
      const createCrimeRateDto: CreateCrimeRateDto = {
        cityId: 1,
        crimeIndex: 50,
        safetyIndex: 50,
        concludedAt: new Date(),
      };

      const createdCrimeRate = { _id: 'mockedId' } as CrimeRateDocument; // Cast to CrimeRateDocument
      jest.spyOn(service, 'createCrimeRate').mockResolvedValue(createdCrimeRate);

      const result = await controller.createCrimeRate(createCrimeRateDto);

      expect(result).toEqual({ id: 'mockedId' });
    });
  });

  describe('getCrimeRates', () => {
    it('should return crime rates based on query parameters', async () => {
      const cityId = 1;
      const size = 2;
      const from = 0;
      const mockCrimeRates = [
        {
          cityId: 1,
          crimeIndex: 50,
          safetyIndex: 50,
          concludedAt: new Date(),
        },
        {
          cityId: 1,
          crimeIndex: 60,
          safetyIndex: 40,
          concludedAt: new Date(),
        },
      ] as CrimeRateDocument[];

      jest.spyOn(service, 'getCrimeRates').mockResolvedValue(mockCrimeRates);

      const result = await controller.getCrimeRates(cityId, size, from);

      expect(result).toBe(mockCrimeRates);
    });
  });

  describe('getCrimeRatesCounts', () => {
    it('should return crime rates counts for specified city IDs', async () => {
      const cityIds = [1];
      const mockCrimeRatesCounts = { 1: 10 };

      jest.spyOn(service, 'getCrimeRatesCount').mockResolvedValue(mockCrimeRatesCounts);

      const result = await controller.getCrimeRatesCounts(cityIds);

      expect(result).toBe(mockCrimeRatesCounts);
    });
  });
});