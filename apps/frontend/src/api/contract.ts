import { makeApi, Zodios, type ZodiosOptions } from "@zodios/core";
import { z } from "zod";

export type CreateCampaignDTO = {
  channel: Channel;
  budget: number;
  startDate: string;
  endDate: string;
};
export type Channel = "RADIO" | "TV" | "SOCIAL_MEDIA" | "SEARCH_ENGINE";
export type UpdateCampaignDTO = Partial<{
  channel: Channel;
  budget: number;
  startDate: string;
  endDate: string;
}>;
export type GetAllCampaignsDTO = Array<CampaignViewModel>;
export type CampaignViewModel = Partial<{
  id: string;
  channel: Channel;
  budget: number;
  startDate: string;
  endDate: string;
}>;

const Channel = z.enum(["RADIO", "TV", "SOCIAL_MEDIA", "SEARCH_ENGINE"]);
const CreateCampaignDTO: z.ZodType<CreateCampaignDTO> = z
  .object({
    channel: Channel,
    budget: z.number(),
    startDate: z.string(),
    endDate: z.string(),
  })
  .passthrough();
const IdResponse = z.object({ id: z.string().uuid() }).partial().passthrough();
const CampaignViewModel: z.ZodType<CampaignViewModel> = z
  .object({
    id: z.string().uuid(),
    channel: Channel,
    budget: z.number(),
    startDate: z.string(),
    endDate: z.string(),
  })
  .partial()
  .passthrough();
const GetAllCampaignsDTO: z.ZodType<GetAllCampaignsDTO> = z.array(CampaignViewModel);
const UpdateCampaignDTO: z.ZodType<UpdateCampaignDTO> = z
  .object({
    channel: Channel,
    budget: z.number(),
    startDate: z.string(),
    endDate: z.string(),
  })
  .partial()
  .passthrough();
const CreateUserDTO = z.object({ email: z.string(), password: z.string() }).passthrough();
const LogInDTO = z.object({ email: z.string(), password: z.string() }).passthrough();
const TokenResponse = z.object({ token: z.string() }).partial().passthrough();

export const schemas = {
  Channel,
  CreateCampaignDTO,
  IdResponse,
  CampaignViewModel,
  GetAllCampaignsDTO,
  UpdateCampaignDTO,
  CreateUserDTO,
  LogInDTO,
  TokenResponse,
};

const endpoints = makeApi([
  {
    method: "post",
    path: "/api/v1/auth/login",
    alias: "postApiv1authlogin",
    requestFormat: "json",
    parameters: [
      {
        name: "body",
        type: "Body",
        schema: LogInDTO,
      },
    ],
    response: z.object({ token: z.string() }).partial().passthrough(),
    errors: [
      {
        status: 400,
        description: `Bad request`,
        schema: z.void(),
      },
    ],
  },
  {
    method: "post",
    path: "/api/v1/auth/register",
    alias: "postApiv1authregister",
    requestFormat: "json",
    parameters: [
      {
        name: "body",
        type: "Body",
        schema: CreateUserDTO,
      },
    ],
    response: z.void(),
    errors: [
      {
        status: 400,
        description: `Bad request`,
        schema: z.void(),
      },
    ],
  },
  {
    method: "post",
    path: "/api/v1/campaigns",
    alias: "postApiv1campaigns",
    requestFormat: "json",
    parameters: [
      {
        name: "body",
        type: "Body",
        schema: CreateCampaignDTO,
      },
    ],
    response: z.object({ id: z.string().uuid() }).partial().passthrough(),
    errors: [
      {
        status: 400,
        description: `Bad request`,
        schema: z.void(),
      },
      {
        status: 401,
        description: `Unauthorized`,
        schema: z.void(),
      },
    ],
  },
  {
    method: "get",
    path: "/api/v1/campaigns",
    alias: "getApiv1campaigns",
    requestFormat: "json",
    response: z.array(CampaignViewModel),
    errors: [
      {
        status: 401,
        description: `Unauthorized`,
        schema: z.void(),
      },
    ],
  },
  {
    method: "get",
    path: "/api/v1/campaigns/:id",
    alias: "getApiv1campaignsId",
    requestFormat: "json",
    parameters: [
      {
        name: "id",
        type: "Path",
        schema: z.string().uuid(),
      },
    ],
    response: CampaignViewModel,
    errors: [
      {
        status: 401,
        description: `Unauthorized`,
        schema: z.void(),
      },
      {
        status: 404,
        description: `Campaign not found`,
        schema: z.void(),
      },
    ],
  },
  {
    method: "patch",
    path: "/api/v1/campaigns/:id",
    alias: "patchApiv1campaignsId",
    requestFormat: "json",
    parameters: [
      {
        name: "body",
        type: "Body",
        schema: UpdateCampaignDTO,
      },
      {
        name: "id",
        type: "Path",
        schema: z.string().uuid(),
      },
    ],
    response: z.void(),
    errors: [
      {
        status: 400,
        description: `Bad request`,
        schema: z.void(),
      },
      {
        status: 401,
        description: `Unauthorized`,
        schema: z.void(),
      },
      {
        status: 404,
        description: `Campaign not found`,
        schema: z.void(),
      },
    ],
  },
  {
    method: "delete",
    path: "/api/v1/campaigns/:id",
    alias: "deleteApiv1campaignsId",
    requestFormat: "json",
    parameters: [
      {
        name: "id",
        type: "Path",
        schema: z.string().uuid(),
      },
    ],
    response: z.void(),
    errors: [
      {
        status: 401,
        description: `Unauthorized`,
        schema: z.void(),
      },
      {
        status: 404,
        description: `Campaign not found`,
        schema: z.void(),
      },
    ],
  },
]);

export const api = new Zodios(endpoints);

export function createApiClient(baseUrl: string, options?: ZodiosOptions) {
  return new Zodios(baseUrl, endpoints, options);
}
