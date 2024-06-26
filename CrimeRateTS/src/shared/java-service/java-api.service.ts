import { BadRequestException, Injectable } from '@nestjs/common';
import { AxiosResponse } from 'axios';
import { lastValueFrom } from 'rxjs';
import { HttpService } from '@nestjs/axios';

@Injectable()
export class JavaApiService {
  constructor(private readonly httpService: HttpService) {
  }

  public async validateCity(id) {
    try {
      const response: AxiosResponse = await lastValueFrom(
        this.httpService.get(`http://localhost:8080/api/city/${id}`),
      );

      const city = response.data;

      if (!city) {
        throw new BadRequestException(`City with id ${id} was not found.`);
      }
    } catch (error) {
      if (error.response && error.response.status === 404) {
        throw new BadRequestException(
          `City with id ${id} was not found.`,
        );
      }
      throw error;
    }
  }
}
