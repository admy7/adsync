import React, { useEffect } from "react";
import { format } from "date-fns";
import { CampaignModal } from "./CampaignModal.tsx";
import { BarChart3, Check, ChevronDown, ChevronUp, Pencil, PlusCircle, Search, Trash2, TrendingUp } from "lucide-react";
import { CampaignViewModel as Campaign } from "../../api/contract.ts";
import { client } from "../../api";
import { Listbox, ListboxButton, ListboxOption, ListboxOptions, Transition } from "@headlessui/react";
import { CampaignKeyFigures } from "./CampaignKeyFigures.tsx";
import { ChannelBarChart } from "./ChannelBarChart.tsx";
import { CampaignTimelineChart } from "./CampaignTimelineChart.tsx";

export type CampaignFormData = Omit<Campaign, "id">;
type SortField = "name" | "channel" | "budget" | "start" | "end";
type SortDirection = "asc" | "desc";

type FilterOption = {
  value: string;
  label: string;
};

const ALL_CHANNELS_OPTION = { value: "all", label: "All Channels" };

export const Dashboard: React.FC = () => {
  const [campaigns, setCampaigns] = React.useState<Campaign[]>([]);
  const [editingCampaign, setEditingCampaign] = React.useState<Campaign | null>(null);
  const [isModalOpen, setIsModalOpen] = React.useState(false);
  const [sortField, setSortField] = React.useState<SortField>("name");
  const [sortDirection, setSortDirection] = React.useState<SortDirection>("asc");
  const [searchTerm, setSearchTerm] = React.useState("");
  const [selectedChannel, setSelectedChannel] = React.useState<FilterOption>(ALL_CHANNELS_OPTION);

  const channelOptions = React.useMemo(() => {
    const channels = Array.from(new Set(campaigns.map((c) => c.channel)));
    return [
      ALL_CHANNELS_OPTION,
      ...channels.map((channel) => ({
        value: channel,
        label: channel,
      })),
    ];
  }, [campaigns]);

  const fetchCampaigns = async () => {
    const { campaigns } = await client.getAllCampaigns();
    setCampaigns(campaigns!);
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
    await fetchCampaigns();
    setEditingCampaign(null);
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

    if (searchTerm) {
      filtered = filtered.filter((campaign) => campaign.name.toLowerCase().includes(searchTerm.toLowerCase()));
    }

    if (selectedChannel.value !== "all") {
      filtered = filtered.filter((campaign) => campaign.channel === selectedChannel.value);
    }

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

  const SortIcon = ({ field }: { field: SortField }) => {
    if (sortField !== field) return null;
    return sortDirection === "asc" ? <ChevronUp className="ml-1 h-4 w-4" /> : <ChevronDown className="ml-1 h-4 w-4" />;
  };

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      {campaigns.length === 0 ? (
        <div className="py-16 text-center">
          <div className="mb-6 flex justify-center space-x-4">
            <div className="rounded-full bg-indigo-100 p-4">
              <BarChart3 className="h-12 w-12 text-indigo-600" />
            </div>
            <div className="rounded-full bg-indigo-100 p-4">
              <TrendingUp className="h-12 w-12 text-indigo-600" />
            </div>
          </div>
          <h2 className="mb-4 text-3xl font-bold text-gray-900">Create Your First Campaign</h2>
          <p className="mx-auto mb-8 max-w-md text-lg text-gray-500">
            Track, analyze, and optimize your marketing campaigns across multiple channels.
          </p>
          <button
            onClick={() => setIsModalOpen(true)}
            className="inline-flex items-center rounded-lg border border-transparent bg-indigo-600 px-6 py-3 text-base font-medium text-white shadow-sm transition-colors duration-150 ease-in-out hover:bg-indigo-700"
          >
            <PlusCircle className="mr-2 h-5 w-5" />
            Create Campaign
          </button>
        </div>
      ) : (
        <>
          <CampaignKeyFigures campaigns={campaigns} />
          <div className="mb-8 grid grid-cols-1 gap-6 md:grid-cols-2">
            <ChannelBarChart campaigns={campaigns} />
            <CampaignTimelineChart campaigns={campaigns} />
          </div>

          <div className="overflow-hidden rounded-lg bg-white shadow">
            <div className="flex flex-col items-start justify-between space-y-4 p-6 sm:flex-row sm:items-center sm:space-y-0">
              <h2 className="text-xl font-semibold text-gray-900">All Campaigns</h2>
              <div className="flex w-full flex-col items-center space-y-4 sm:w-auto sm:flex-row sm:space-x-4 sm:space-y-0">
                <div className="relative flex-grow sm:flex-grow-0">
                  <div className="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
                    <Search className="h-5 w-5 text-gray-400" />
                  </div>
                  <input
                    type="text"
                    placeholder="Search campaigns..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="block w-full rounded-lg border border-gray-300 py-3 pl-10 pr-3 text-base focus:border-indigo-500 focus:ring-indigo-500"
                  />
                </div>
                <div className="w-full sm:w-[200px]">
                  <Listbox value={selectedChannel} onChange={setSelectedChannel}>
                    <div className="relative">
                      <ListboxButton className="relative w-full cursor-pointer rounded-lg border border-gray-300 bg-white py-3 pl-4 pr-10 text-left text-base focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500">
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
                        <ListboxOptions className="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-lg bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
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
                  className="inline-flex items-center rounded-lg border border-transparent bg-indigo-600 px-4 py-3 text-sm font-medium text-white shadow-sm hover:bg-indigo-700"
                >
                  <PlusCircle className="mr-2 h-4 w-4" />
                  New Campaign
                </button>
              </div>
            </div>
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th
                      className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 hover:bg-gray-100"
                      onClick={() => handleSort("name")}
                    >
                      <div className="flex items-center">
                        Name
                        <SortIcon field="name" />
                      </div>
                    </th>
                    <th
                      className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 hover:bg-gray-100"
                      onClick={() => handleSort("channel")}
                    >
                      <div className="flex items-center">
                        Channel
                        <SortIcon field="channel" />
                      </div>
                    </th>
                    <th
                      className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 hover:bg-gray-100"
                      onClick={() => handleSort("budget")}
                    >
                      <div className="flex items-center">
                        Budget
                        <SortIcon field="budget" />
                      </div>
                    </th>
                    <th
                      className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 hover:bg-gray-100"
                      onClick={() => handleSort("start")}
                    >
                      <div className="flex items-center">
                        Start Date
                        <SortIcon field="start" />
                      </div>
                    </th>
                    <th
                      className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 hover:bg-gray-100"
                      onClick={() => handleSort("end")}
                    >
                      <div className="flex items-center">
                        End Date
                        <SortIcon field="end" />
                      </div>
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200 bg-white">
                  {sortedAndFilteredCampaigns.map((campaign) => (
                    <tr key={campaign.id} className="hover:bg-gray-50">
                      <td className="whitespace-nowrap px-6 py-4">{campaign.name}</td>
                      <td className="whitespace-nowrap px-6 py-4">{campaign.channel}</td>
                      <td className="whitespace-nowrap px-6 py-4">{campaign.budget.toLocaleString()}€</td>
                      <td className="whitespace-nowrap px-6 py-4">{format(new Date(campaign.start), "MMM d, yyyy")}</td>
                      <td className="whitespace-nowrap px-6 py-4">{format(new Date(campaign.end), "MMM d, yyyy")}</td>
                      <td className="whitespace-nowrap px-6 py-4">
                        <div className="flex space-x-2">
                          <button
                            onClick={() => {
                              setEditingCampaign(campaign);
                              setIsModalOpen(true);
                            }}
                            className="text-indigo-600 hover:text-indigo-900"
                          >
                            <Pencil className="h-4 w-4" />
                          </button>
                          <button
                            onClick={() => handleDeleteCampaign(campaign.id)}
                            className="text-red-600 hover:text-red-900"
                          >
                            <Trash2 className="h-4 w-4" />
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
};
