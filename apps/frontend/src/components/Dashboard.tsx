import React, { useEffect } from "react";
import { format } from "date-fns";
import { Bar, BarChart, CartesianGrid, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import { CampaignModal } from "./CampaignModal.tsx";
import { BarChart3, Pencil, PlusCircle, Trash2 } from "lucide-react";
import { CampaignViewModel as Campaign } from "../api/contract.ts";
import { client } from "../api";

export type CampaignFormData = Omit<Campaign, "id">;

export const Dashboard: React.FC = () => {
    const [campaigns, setCampaigns] = React.useState<Campaign[]>([]);
    const [isModalOpen, setIsModalOpen] = React.useState(false);
    const [editingCampaign, setEditingCampaign] = React.useState<Campaign | null>(
      null,
    );

    const fetchCampaigns = async () => {
      try {
        const { campaigns } = await client.getAllCampaigns();
        setCampaigns(campaigns!);
      } catch (error) {
        if (error instanceof Error) {
          console.error(error.message);
        } else {
          console.error("An unknown error occurred.");
        }
      }
    };

    useEffect(() => {
      fetchCampaigns();
      console.log(campaigns);
    }, []);

    const handleCreateCampaign = async (data: CampaignFormData) => {
      const formattedData = {
        ...data,
        start: format(new Date(data.start), "yyyy-MM-dd'T'HH:mm:ss"),
        end: format(new Date(data.end), "yyyy-MM-dd'T'HH:mm:ss"),
      };
      await client.createCampaign(formattedData);
      await fetchCampaigns();
    };

    const handleEditCampaign = async (data: CampaignFormData) => {
      if (!editingCampaign) {
        return;
      }

      const formattedData = {
        ...data,
        start: format(new Date(data.start), "yyyy-MM-dd'T'HH:mm:ss"),
        end: format(new Date(data.end), "yyyy-MM-dd'T'HH:mm:ss"),
      };

      await client.updateCampaign(formattedData, { params: { id: editingCampaign.id! } });
      setEditingCampaign(null);
      await fetchCampaigns();
    };

    const handleDeleteCampaign = async (id: string) => {
      await client.deleteCampaign(undefined, { params: { id } });
      await fetchCampaigns();
    };

    const totalBudget = campaigns.reduce((sum, campaign) => sum + campaign.budget, 0);

    const channelData = campaigns.reduce((acc: any[], campaign) => {
      const existing = acc.find((item) => item.channel === campaign.channel);
      if (existing) {
        existing.budget += campaign.budget;
      } else {
        acc.push({ channel: campaign.channel, budget: campaign.budget });
      }
      return acc;
    }, []);

    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {campaigns.length === 0 ? (
          <div className="text-center py-12">
            <div className="flex justify-center space-x-4 mb-6">
              <div className="bg-indigo-100 p-4 rounded-full">
                <BarChart3 className="h-12 w-12 text-indigo-600" />
              </div>
            </div>
            <h2 className="text-2xl font-semibold text-gray-900 mb-4">
              Create Your First Campaign
            </h2>
            <p className="text-gray-500 mb-6">
              Track, analyze, and optimize your marketing campaigns across multiple channels.
            </p>
            <button
              onClick={() => setIsModalOpen(true)}
              className="inline-flex items-center px-4 py-2 border border-transparent text-base font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700"
            >
              <PlusCircle className="w-5 h-5 mr-2" />
              Create Campaign
            </button>
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
              <div className="bg-white p-6 rounded-lg shadow">
                <h3 className="text-lg font-medium text-gray-900 mb-2">
                  Total Campaigns
                </h3>
                <p className="text-3xl font-bold text-indigo-600">
                  {campaigns.length}
                </p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow">
                <h3 className="text-lg font-medium text-gray-900 mb-2">
                  Total Budget
                </h3>
                <p className="text-3xl font-bold text-indigo-600">
                  €{totalBudget.toLocaleString()}
                </p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow">
                <h3 className="text-lg font-medium text-gray-900 mb-2">
                  Active Campaigns
                </h3>
                <p className="text-3xl font-bold text-indigo-600">
                  {
                    campaigns.filter(
                      (campaign) =>
                        new Date(campaign.end) >= new Date(),
                    ).length
                  }
                </p>
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
              <div className="bg-white p-6 rounded-lg shadow">
                <h3 className="text-lg font-medium text-gray-900 mb-4">
                  Budget by Channel
                </h3>
                <div className="h-64">
                  <ResponsiveContainer width="100%" height="100%">
                    <BarChart data={channelData}>
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="channel" />
                      <YAxis />
                      <Tooltip />
                      <Bar dataKey="budget" fill="#4f46e5" />
                    </BarChart>
                  </ResponsiveContainer>
                </div>
              </div>
              <div className="bg-white p-6 rounded-lg shadow">
                <h3 className="text-lg font-medium text-gray-900 mb-4">
                  Campaign Timeline
                </h3>
                <div className="h-64">
                  <ResponsiveContainer width="100%" height="100%">
                    <LineChart
                      data={campaigns.map((campaign) => ({
                        name: campaign.channel,
                        date: campaign.start,
                        budget: campaign.budget,
                      }))}
                    >
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="date" />
                      <YAxis />
                      <Tooltip />
                      <Line
                        type="monotone"
                        dataKey="budget"
                        stroke="#4f46e5"
                        strokeWidth={2}
                      />
                    </LineChart>
                  </ResponsiveContainer>
                </div>
              </div>
            </div>

            <div className="bg-white shadow rounded-lg overflow-hidden">
              <div className="p-6 flex justify-between items-center">
                <h2 className="text-xl font-semibold text-gray-900">
                  All Campaigns
                </h2>
                <button
                  onClick={() => setIsModalOpen(true)}
                  className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700"
                >
                  <PlusCircle className="w-4 h-4 mr-2" />
                  New Campaign
                </button>
              </div>
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Name
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Channel
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Budget
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Start Date
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      End Date
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                  {campaigns.map((campaign) => (
                    <tr key={campaign.id}>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {campaign.name}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {campaign.channel}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        €{campaign.budget.toLocaleString()}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {format(new Date(campaign.start), "MMM d, yyyy")}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {format(new Date(campaign.end), "MMM d, yyyy")}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex space-x-2">
                          <button
                            onClick={() => {
                              setEditingCampaign(campaign);
                              setIsModalOpen(true);
                            }}
                            className="text-indigo-600 hover:text-indigo-900"
                          >
                            <Pencil className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => handleDeleteCampaign(campaign.id)}
                            className="text-red-600 hover:text-red-900"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                  </tbody>
                </table>
              </div>
            </div>
          </>
        )}

        <CampaignModal
          isOpen={isModalOpen}
          onClose={() => {
            setIsModalOpen(false);
            setEditingCampaign(null);
          }}
          onSubmit={editingCampaign ? handleEditCampaign : handleCreateCampaign}
          initialData={editingCampaign || undefined}
          mode={editingCampaign ? "edit" : "create"}
        />
      </div>
    );
  }
;