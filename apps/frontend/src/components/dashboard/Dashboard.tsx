import React, { useEffect } from "react";
import { eachDayOfInterval, format, parseISO } from "date-fns";
import {
  Bar,
  BarChart,
  CartesianGrid,
  Legend,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";
import { CampaignModal } from "./CampaignModal.tsx";
import { BarChart3, Check, ChevronDown, ChevronUp, Pencil, PlusCircle, Search, Trash2, TrendingUp } from "lucide-react";
import { CampaignViewModel as Campaign } from "../../api/contract.ts";
import { client } from "../../api";
import { Listbox, ListboxButton, ListboxOption, ListboxOptions, Transition } from "@headlessui/react";
import { CampaignKeyFigures } from "./CampaignKeyFigures.tsx";
import { ChannelBarChart } from "./ChannelBarChart.tsx";

export type CampaignFormData = Omit<Campaign, "id">;
type SortField = "name" | "channel" | "budget" | "start" | "end";
type SortDirection = "asc" | "desc";

interface FilterOption {
  value: string;
  label: string;
}

const ALL_CHANNELS_OPTION = { value: "all", label: "All Channels" };

export const Dashboard: React.FC = () => {
    const [campaigns, setCampaigns] = React.useState<Campaign[]>([]);
    const [isModalOpen, setIsModalOpen] = React.useState(false);
    const [editingCampaign, setEditingCampaign] = React.useState<Campaign | null>(
      null,
    );
    const [sortField, setSortField] = React.useState<SortField>("name");
    const [sortDirection, setSortDirection] = React.useState<SortDirection>("asc");
    const [searchTerm, setSearchTerm] = React.useState("");
    const [selectedChannel, setSelectedChannel] = React.useState<FilterOption>(ALL_CHANNELS_OPTION);

    const channelOptions = React.useMemo(() => {
      const channels = Array.from(new Set(campaigns.map(c => c.channel)));
      return [
        ALL_CHANNELS_OPTION,
        ...channels.map(channel => ({
          value: channel,
          label: channel,
        })),
      ];
    }, [campaigns]);

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

    const handleSort = (field: SortField) => {
      if (sortField === field) {
        setSortDirection(sortDirection === "asc" ? "desc" : "asc");
      } else {
        setSortField(field);
        setSortDirection("asc");
      }
    };

    const sortedAndFilteredCampaigns = React.useMemo(() => {
      let filtered = campaigns;

      // Apply search filter
      if (searchTerm) {
        filtered = filtered.filter(campaign =>
          campaign.name.toLowerCase().includes(searchTerm.toLowerCase()),
        );
      }

      // Apply channel filter
      if (selectedChannel.value !== "all") {
        filtered = filtered.filter(campaign => campaign.channel === selectedChannel.value);
      }

      // Apply sorting
      return [...filtered].sort((a, b) => {
        const aValue = a[sortField];
        const bValue = b[sortField];

        const direction = sortDirection === "asc" ? 1 : -1;

        if (typeof aValue === "string" && typeof bValue === "string") {
          return direction * aValue.localeCompare(bValue);
        }

        if (typeof aValue === "number" && typeof bValue === "number") {
          return direction * (aValue - bValue);
        }

        return 0;
      });
    }, [campaigns, sortField, sortDirection, searchTerm, selectedChannel]);
    const totalBudget = campaigns.reduce((sum, campaign) => sum + campaign.budget, 0);


    const timelineData = React.useMemo(() => {
      if (campaigns.length === 0) return [];

      // Find the date range for all campaigns
      const startDates = campaigns.map(c => parseISO(c.start));
      const endDates = campaigns.map(c => parseISO(c.end));
      const minDate = new Date(Math.min(...startDates.map(d => d.getTime())));
      const maxDate = new Date(Math.max(...endDates.map(d => d.getTime())));

      // Generate array of dates between min and max
      const dates = eachDayOfInterval({ start: minDate, end: maxDate });

      // Create data points for each date
      return dates.map(date => {
        const dataPoint: any = {
          date: format(date, "yyyy-MM-dd"),
          total: 0,
        };

        // Calculate budget for each campaign on this date
        campaigns.forEach(campaign => {
          const campaignStart = parseISO(campaign.start);
          const campaignEnd = parseISO(campaign.end);
          //
          if (date >= campaignStart && date <= campaignEnd) {
            //   // Calculate daily budget (total budget divided by campaign duration)
            //   const duration = Math.ceil((campaignEnd.getTime() - campaignStart.getTime()) / (1000 * 60 * 60 * 24));
            //   const dailyBudget = campaign.budget / duration;

            dataPoint[campaign.name] = campaign.budget;
            dataPoint.total += campaign.budget;
          } else {
            dataPoint[campaign.name] = 0;
          }
        });

        return dataPoint;
      });
    }, [campaigns]);

    // Generate unique colors for each campaign
    const campaignColors = React.useMemo(() => {
      const colors = [
        "#4f46e5", // indigo
        "#06b6d4", // cyan
        "#10b981", // emerald
        "#f59e0b", // amber
        "#ef4444", // red
        "#8b5cf6", // violet
        "#ec4899", // pink
        "#f97316", // orange
      ];

      return campaigns.reduce((acc, campaign, index) => ({
        ...acc,
        [campaign.name]: colors[index % colors.length],
      }), {});
    }, [campaigns]);

    const SortIcon = ({ field }: { field: SortField }) => {
      if (sortField !== field) return null;
      return sortDirection === "asc" ? (
        <ChevronUp className="w-4 h-4 ml-1" />
      ) : (
        <ChevronDown className="w-4 h-4 ml-1" />
      );
    };

    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {campaigns.length === 0 ? (
          <div className="text-center py-16">
            <div className="flex justify-center space-x-4 mb-6">
              <div className="bg-indigo-100 p-4 rounded-full">
                <BarChart3 className="h-12 w-12 text-indigo-600" />
              </div>
              <div className="bg-indigo-100 p-4 rounded-full">
                <TrendingUp className="h-12 w-12 text-indigo-600" />
              </div>
            </div>
            <h2 className="text-3xl font-bold text-gray-900 mb-4">
              Create Your First Campaign
            </h2>
            <p className="text-lg text-gray-500 mb-8 max-w-md mx-auto">
              Track, analyze, and optimize your marketing campaigns across multiple channels.
            </p>
            <button
              onClick={() => setIsModalOpen(true)}
              className="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-lg shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 transition-colors duration-150 ease-in-out"
            >
              <PlusCircle className="w-5 h-5 mr-2" />
              Create Campaign
            </button>
          </div>
        ) : (
          <>
            <CampaignKeyFigures campaigns={campaigns} totalBudget={totalBudget} />
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
              <ChannelBarChart campaigns={campaigns} />
              <div className="bg-white p-6 rounded-lg shadow">
                <h3 className="text-lg font-medium text-gray-900 mb-4">
                  Campaign Timeline
                </h3>
                <div className="h-96">
                  <ResponsiveContainer width="100%" height="100%">
                    <LineChart
                      data={timelineData}
                      margin={{
                        top: 5,
                        right: 30,
                        left: 20,
                        bottom: 5,
                      }}
                    >
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis
                        dataKey="date"
                        tickFormatter={(date) => format(parseISO(date), "MMM yy")}
                      />
                      <YAxis
                        tickFormatter={(value) => `${value.toLocaleString()}€`}
                      />
                      <Tooltip
                        formatter={(value: number) => [`${value.toLocaleString()}€`, ""]}
                        labelFormatter={(label) => format(parseISO(label as string), "MMM d, yyyy")}
                      />
                      <Legend />
                      {campaigns.map((campaign) => (
                        <Line
                          key={campaign.id}
                          type="monotone"
                          dataKey={campaign.name}
                          stroke={campaignColors[campaign.name]}
                          strokeWidth={2}
                          dot={false}
                        />
                      ))}
                      <Line
                        type="monotone"
                        dataKey="total"
                        stroke="#000000"
                        strokeWidth={3}
                        dot={false}
                        name="Total Budget"
                      />
                    </LineChart>
                  </ResponsiveContainer>
                </div>
              </div>
            </div>

            <div className="bg-white shadow rounded-lg overflow-hidden">
              <div
                className="p-6 flex flex-col sm:flex-row justify-between items-start sm:items-center space-y-4 sm:space-y-0">
                <h2 className="text-xl font-semibold text-gray-900">
                  All Campaigns
                </h2>
                <div
                  className="flex flex-col items-center sm:flex-row space-y-4 sm:space-y-0 sm:space-x-4 w-full sm:w-auto">
                  <div className="relative flex-grow sm:flex-grow-0">
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <Search className="h-5 w-5 text-gray-400" />
                    </div>
                    <input
                      type="text"
                      placeholder="Search campaigns..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                      className="block w-full pl-10 pr-3 py-3 text-base border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500"
                    />
                  </div>
                  <div className="w-full sm:w-[200px]">
                    <Listbox value={selectedChannel} onChange={setSelectedChannel}>
                      <div className="relative">
                        <ListboxButton
                          className="relative w-full cursor-pointer rounded-lg bg-white py-3 pl-4 pr-10 text-left text-base border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
                          <span className="block truncate">{selectedChannel.label}</span>
                          <span className="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-3">
                          <ChevronDown className="h-5 w-5 text-gray-400" aria-hidden="true" />
                        </span>
                        </ListboxButton>
                        <Transition
                          as={React.Fragment}
                          leave="transition ease-in duration-100"
                          leaveFrom="opacity-100"
                          leaveTo="opacity-0"
                        >
                          <ListboxOptions
                            className="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-lg bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
                            {channelOptions.map((option) => (
                              <ListboxOption
                                key={option.value}
                                className={({ focus }) =>
                                  `relative cursor-pointer select-none py-3 pl-10 pr-4 ${
                                    focus ? "bg-indigo-50 text-indigo-900" : "text-gray-900"
                                  }`
                                }
                                value={option}
                              >
                                {({ selected }) => (
                                  <>
                                  <span className={`block truncate ${selected ? "font-medium" : "font-normal"}`}>
                                    {option.label}
                                  </span>
                                    {selected ? (
                                      <span className="absolute inset-y-0 left-0 flex items-center pl-3 text-indigo-600">
                                      <Check className="h-5 w-5" aria-hidden="true" />
                                    </span>
                                    ) : null}
                                  </>
                                )}
                              </ListboxOption>
                            ))}
                          </ListboxOptions>
                        </Transition>
                      </div>
                    </Listbox>
                  </div>
                  <button
                    onClick={() => setIsModalOpen(true)}
                    className="inline-flex items-center px-4 py-3 border border-transparent text-sm font-medium rounded-lg shadow-sm text-white bg-indigo-600 hover:bg-indigo-700"
                  >
                    <PlusCircle className="w-4 h-4 mr-2" />
                    New Campaign
                  </button>
                </div>
              </div>
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                  <tr>
                    <th
                      className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
                      onClick={() => handleSort("name")}
                    >
                      <div className="flex items-center">
                        Name
                        <SortIcon field="name" />
                      </div>
                    </th>
                    <th
                      className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
                      onClick={() => handleSort("channel")}
                    >
                      <div className="flex items-center">
                        Channel
                        <SortIcon field="channel" />
                      </div>
                    </th>
                    <th
                      className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
                      onClick={() => handleSort("budget")}
                    >
                      <div className="flex items-center">
                        Budget
                        <SortIcon field="budget" />
                      </div>
                    </th>
                    <th
                      className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
                      onClick={() => handleSort("start")}
                    >
                      <div className="flex items-center">
                        Start Date
                        <SortIcon field="start" />
                      </div>
                    </th>
                    <th
                      className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
                      onClick={() => handleSort("end")}
                    >
                      <div className="flex items-center">
                        End Date
                        <SortIcon field="end" />
                      </div>
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                  {sortedAndFilteredCampaigns.map((campaign) => (
                    <tr key={campaign.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        {campaign.name}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {campaign.channel}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {campaign.budget.toLocaleString()}€
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