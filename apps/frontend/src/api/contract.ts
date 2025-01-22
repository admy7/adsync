import { makeApi, Zodios, type ZodiosOptions } from "@zodios/core";
import { z } from "zod";

export type CreateCampaignDTO = {
    name: string;
    channel: Channel;
    budget: number;
    start: string;
    end: string;
};;
export type Channel = string;;
export type UpdateCampaignDTO = Partial<{
    name: string;
    channel: Channel;
    budget: number;
    start: string;
    end: string;
}>;;
export type GetAllCampaignsDTO = Partial<{
    count: unknown;
    campaigns: Array<CampaignViewModel>;
}>;;
export type CampaignViewModel = {
    id: string;
    name: string;
    channel: Channel;
    budget: number;
    start: string;
    end: string;
};;

export const Channel = z.string();
const CreateCampaignDTO: z.ZodType<CreateCampaignDTO> = z.object({ name: z.string(), channel: Channel, budget: z.number(), start: z.string(), end: z.string() }).passthrough();
const IdResponse = z.object({ id: z.string().uuid() }).partial().passthrough();
const CampaignViewModel: z.ZodType<CampaignViewModel> = z.object({ id: z.string().uuid(), name: z.string(), channel: Channel, budget: z.number(), start: z.string(), end: z.string() }).passthrough();
const GetAllCampaignsDTO: z.ZodType<GetAllCampaignsDTO> = z.object({ count: z.unknown(), campaigns: z.array(CampaignViewModel) }).partial().passthrough();
const UpdateCampaignDTO: z.ZodType<UpdateCampaignDTO> = z.object({ name: z.string(), channel: Channel, budget: z.number(), start: z.string(), end: z.string() }).partial().passthrough();
const CreateUserDTO = z.object({ email: z.string(), password: z.string() }).passthrough();
const LogInDTO = z.object({ email: z.string(), password: z.string() }).passthrough();
const TokenResponse = z.object({ token: z.string() }).passthrough();

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
		alias: "loginUser",
		requestFormat: "json",
		parameters: [
			{
				name: "body",
				type: "Body",
				schema: LogInDTO
			},
		],
		response: z.object({ token: z.string() }).passthrough(),
		errors: [
			{
				status: 400,
				description: `Bad request`,
				schema: z.void()
			},
		]
	},
	{
		method: "post",
		path: "/api/v1/auth/register",
		alias: "registerUser",
		requestFormat: "json",
		parameters: [
			{
				name: "body",
				type: "Body",
				schema: CreateUserDTO
			},
		],
		response: z.void(),
		errors: [
			{
				status: 400,
				description: `Bad request`,
				schema: z.void()
			},
		]
	},
	{
		method: "post",
		path: "/api/v1/campaigns",
		alias: "createCampaign",
		requestFormat: "json",
		parameters: [
			{
				name: "body",
				type: "Body",
				schema: CreateCampaignDTO
			},
		],
		response: z.object({ id: z.string().uuid() }).partial().passthrough(),
		errors: [
			{
				status: 400,
				description: `Bad request`,
				schema: z.void()
			},
			{
				status: 401,
				description: `Unauthorized`,
				schema: z.void()
			},
		]
	},
	{
		method: "get",
		path: "/api/v1/campaigns",
		alias: "getAllCampaigns",
		requestFormat: "json",
		response: GetAllCampaignsDTO,
		errors: [
			{
				status: 401,
				description: `Unauthorized`,
				schema: z.void()
			},
		]
	},
	{
		method: "get",
		path: "/api/v1/campaigns/:id",
		alias: "getCampaign",
		requestFormat: "json",
		parameters: [
			{
				name: "id",
				type: "Path",
				schema: z.string().uuid()
			},
		],
		response: CampaignViewModel,
		errors: [
			{
				status: 401,
				description: `Unauthorized`,
				schema: z.void()
			},
			{
				status: 404,
				description: `Campaign not found`,
				schema: z.void()
			},
		]
	},
	{
		method: "patch",
		path: "/api/v1/campaigns/:id",
		alias: "updateCampaign",
		requestFormat: "json",
		parameters: [
			{
				name: "body",
				type: "Body",
				schema: UpdateCampaignDTO
			},
			{
				name: "id",
				type: "Path",
				schema: z.string().uuid()
			},
		],
		response: z.void(),
		errors: [
			{
				status: 400,
				description: `Bad request`,
				schema: z.void()
			},
			{
				status: 401,
				description: `Unauthorized`,
				schema: z.void()
			},
			{
				status: 404,
				description: `Campaign not found`,
				schema: z.void()
			},
		]
	},
	{
		method: "delete",
		path: "/api/v1/campaigns/:id",
		alias: "deleteCampaign",
		requestFormat: "json",
		parameters: [
			{
				name: "id",
				type: "Path",
				schema: z.string().uuid()
			},
		],
		response: z.void(),
		errors: [
			{
				status: 401,
				description: `Unauthorized`,
				schema: z.void()
			},
			{
				status: 404,
				description: `Campaign not found`,
				schema: z.void()
			},
		]
	},
]);

export const api = new Zodios(endpoints);

export function createApiClient(baseUrl: string, options?: ZodiosOptions) {
    return new Zodios(baseUrl, endpoints, options);
}
