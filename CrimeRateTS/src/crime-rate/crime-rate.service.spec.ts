import { Test, TestingModule } from "@nestjs/testing";
import { MongooseModule } from "@nestjs/mongoose";
import { CrimeRateService } from "./crime-rate.service";
import { CrimeRate, CrimeRateSchema } from "./crime-rate.schema";
import { JavaApiService } from "../shared/java-service/java-api.service";
import mongoSetup from "../../test/mongoSetup";
import mongoose from "mongoose";
import { BadRequestException } from "@nestjs/common";

describe("CrimeRateService", () => {
  let service: CrimeRateService;
  let uri: string;

  beforeAll(async () => {
    uri = await mongoSetup;

    const module: TestingModule = await Test.createTestingModule({
      imports: [
        MongooseModule.forRoot(uri),
        MongooseModule.forFeature([{ name: CrimeRate.name, schema: CrimeRateSchema }])
      ],
      providers: [
        CrimeRateService,
        {
          provide: JavaApiService,
          useValue: {
            validateCity: jest.fn().mockResolvedValue(true)
          }
        }
      ]
    }).compile();

    service = module.get<CrimeRateService>(CrimeRateService);
  });

  afterAll(async () => {
    await mongoose.connection.close();
  });

  it("should create a new crime rate", async () => {
    const createCrimeRateDto = {
      cityId: 1,
      crimeIndex: 50,
      safetyIndex: 50,
      concludedAt: new Date()
    };

    const crimeRate = await service.createCrimeRate(createCrimeRateDto);
    expect(crimeRate).toBeDefined();
    expect(crimeRate.cityId).toBe(createCrimeRateDto.cityId);
    expect(crimeRate.crimeIndex).toBe(createCrimeRateDto.crimeIndex);
    expect(crimeRate.safetyIndex).toBe(createCrimeRateDto.safetyIndex);
  });

  it("should throw BadRequestException when trying to save a duplicate record", async () => {
    const createCrimeRateDto = {
      cityId: 1,
      crimeIndex: 50,
      safetyIndex: 50,
      concludedAt: new Date()
    };

    await service.createCrimeRate(createCrimeRateDto);

    try {
      await service.createCrimeRate(createCrimeRateDto);
      fail("Expected BadRequestException was not thrown");
    } catch (error) {
      expect(error).toBeInstanceOf(BadRequestException);
      expect(error.message).toBe(
        "A record with the same cityId, crimeIndex, safetyIndex, and concludedAt already exists."
      );
    }
  });

  it("should retrieve crime rates by cityId", async () => {
    const cityId = 1;
    const size = 10;
    const from = 0;

    const crimeRates = await service.getCrimeRates(cityId, size, from);
    expect(crimeRates.length).toBeGreaterThan(0);
    expect(crimeRates[0].cityId).toBe(cityId);
  });

  it("should throw BadRequestException when trying to pass cityId undefined", async () => {
    const size = 10;
    const from = 0;

    try {
      await service.getCrimeRates(undefined, size, from);
      fail("Expected BadRequestException was not thrown");
    } catch (error) {
      expect(error).toBeInstanceOf(BadRequestException);
      expect(error.message).toBe(
        "cityId is required."
      );
    }
  });

  it("should throw BadRequestException when trying to pass size less then zero", async () => {
    const cityId = 1;
    const size = -1;
    const from = 0;

    try {
      await service.getCrimeRates(cityId, size, from);
      fail("Expected BadRequestException was not thrown");
    } catch (error) {
      expect(error).toBeInstanceOf(BadRequestException);
      expect(error.message).toBe(
        "size has to be more then 0."
      );
    }
  });

  it("should throw BadRequestException when trying to pass from less then zero", async () => {
    const cityId = 1;
    const size = 10;
    const from = -1;

    try {
      await service.getCrimeRates(cityId, size, from);
      fail("Expected BadRequestException was not thrown");
    } catch (error) {
      expect(error).toBeInstanceOf(BadRequestException);
      expect(error.message).toBe(
        "from has to be more or equal 0."
      );
    }
  });

  it("should count crime rates by cityIds", async () => {
    const cityIds = [1];

    const counts = await service.getCrimeRatesCount(cityIds);
    expect(counts[1]).toBeGreaterThan(0);
  });

  it("should count crime rates throw BadRequestException when cityIds is not array", async () => {
    try {
      await service.getCrimeRatesCount(undefined);
      fail("Expected BadRequestException was not thrown");
    } catch (error) {
      expect(error).toBeInstanceOf(BadRequestException);
      expect(error.message).toBe(
        "You have to add cityIds array in your request body"
      );
    }
  });
});