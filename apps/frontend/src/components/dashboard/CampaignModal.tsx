import React, { useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  Listbox,
  ListboxButton,
  ListboxOption,
  ListboxOptions,
  Transition,
  TransitionChild,
} from "@headlessui/react";
import { Check, ChevronDown } from "lucide-react";
import { Channel } from "../../api/contract.ts";
import { CampaignFormData } from "./Dashboard.tsx";
import { format } from "date-fns";

type CampaignModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: CampaignFormData) => void;
  initialData?: CampaignFormData;
  mode: "create" | "edit";
};

const channels: Channel[] = ["Radio", "TV", "Social media", "Search engine"];

export const CampaignModal: React.FC<CampaignModalProps> = ({ isOpen, onClose, onSubmit, initialData, mode }) => {
  const [formData, setFormData] = React.useState<CampaignFormData>(
    initialData || {
      name: "",
      channel: "Radio",
      budget: 0,
      start: "",
      end: "",
    },
  );
  const [error, setError] = React.useState<string | null>(null);

  useEffect(() => {
    if (isOpen) {
      setFormData(
        initialData || {
          name: "",
          channel: "Radio",
          budget: 0,
          start: "",
          end: "",
        },
      );
    }
    setError(null);
  }, [isOpen, initialData]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (new Date(formData.start) > new Date(formData.end)) {
      setError("End date must be after start date");
      return;
    }

    onSubmit(formData);
    setError(null);
    onClose();
  };

  const inputClassName =
    "focus:ring-indigo-500 focus:ring-opacity-50 focus:border-indigo-500 focus:ring-2 focus:ring-opacity 5 focus:outline-none mt-1 block w-full px-4 py-3 text-base rounded-lg border-gray-300 border transition-colors duration-200";

  return (
    <Transition show={isOpen} as={React.Fragment}>
      <Dialog as="div" className="fixed inset-0 z-10 overflow-y-auto" onClose={onClose}>
        <div className="min-h-screen px-4 text-center">
          <TransitionChild
            as={React.Fragment}
            enter="ease-out duration-300"
            enterFrom="opacity-0"
            enterTo="opacity-100"
            leave="ease-in duration-200"
            leaveFrom="opacity-100"
            leaveTo="opacity-0"
          >
            <div className="fixed inset-0 bg-black opacity-30" aria-hidden="true" />
          </TransitionChild>

          <span className="inline-block h-screen align-middle" aria-hidden="true">
            &#8203;
          </span>

          <TransitionChild
            as={React.Fragment}
            enter="ease-out duration-300"
            enterFrom="opacity-0 scale-95"
            enterTo="opacity-100 scale-100"
            leave="ease-in duration-200"
            leaveFrom="opacity-100 scale-100"
            leaveTo="opacity-0 scale-95"
          >
            <div className="my-8 inline-block w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-8 text-left align-middle shadow-xl transition-all">
              <DialogTitle as="h3" className="text-xl font-semibold leading-6 text-gray-900">
                {mode === "create" ? "Create Campaign" : "Edit Campaign"}
              </DialogTitle>

              <form onSubmit={handleSubmit} className="mt-6">
                <div className="space-y-6">
                  <div>
                    <label className="mb-1 block text-sm font-medium text-gray-700">Campaign Name</label>
                    <input
                      type="text"
                      className={inputClassName}
                      value={formData.name}
                      onChange={(e) =>
                        setFormData({
                          ...formData,
                          name: e.target.value,
                        })
                      }
                      placeholder="Enter campaign name"
                      required
                    />
                  </div>

                  <div>
                    <label className="mb-1 block text-sm font-medium text-gray-700">Channel</label>
                    <Listbox
                      value={formData.channel}
                      onChange={(channel) =>
                        setFormData({
                          ...formData,
                          channel,
                        })
                      }
                    >
                      <div className="relative mt-1">
                        <ListboxButton className="relative w-full cursor-pointer rounded-lg border border-gray-300 bg-white py-3 pl-4 pr-10 text-left text-base transition-colors duration-200 focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-opacity-50">
                          <span className="block truncate">{formData.channel}</span>
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
                            {channels.map((channel) => (
                              <ListboxOption
                                key={channel}
                                className={({ focus }) =>
                                  `relative cursor-pointer select-none py-3 pl-10 pr-4 ${
                                    focus ? "bg-indigo-50 text-indigo-900" : "text-gray-900"
                                  }`
                                }
                                value={channel}
                              >
                                {({ selected }) => (
                                  <>
                                    <span className={`block truncate ${selected ? "font-medium" : "font-normal"}`}>
                                      {channel}
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

                  <div>
                    <label className="mb-1 block text-sm font-medium text-gray-700">Budget (€)</label>
                    <input
                      type="number"
                      min="0"
                      step="1"
                      className={inputClassName}
                      value={formData.budget || ""}
                      onChange={(e) =>
                        setFormData({
                          ...formData,
                          budget: e.target.value ? Number(e.target.value) : 0,
                        })
                      }
                      required
                      placeholder="Enter budget amount"
                    />
                  </div>

                  <div>
                    <label className="mb-1 block text-sm font-medium text-gray-700">Start Date</label>
                    <input
                      type="date"
                      className={inputClassName}
                      value={formData.start ? format(new Date(formData.start), "yyyy-MM-dd") : ""}
                      onChange={(e) =>
                        setFormData({
                          ...formData,
                          start: e.target.value,
                        })
                      }
                      required
                    />
                  </div>

                  <div>
                    <label className="mb-1 block text-sm font-medium text-gray-700">End Date</label>
                    <input
                      type="date"
                      className={inputClassName}
                      value={formData.end ? format(new Date(formData.end), "yyyy-MM-dd") : ""}
                      onChange={(e) =>
                        setFormData({
                          ...formData,
                          end: e.target.value,
                        })
                      }
                      required
                    />
                  </div>
                  {error && <div className="text-sm text-red-500">{error}</div>}
                </div>

                <div className="mt-8 flex justify-end space-x-3">
                  <button
                    type="button"
                    className="rounded-lg bg-gray-100 px-5 py-3 text-sm font-medium text-gray-700 transition-colors duration-200 hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                    onClick={onClose}
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="rounded-lg bg-indigo-600 px-5 py-3 text-sm font-medium text-white transition-colors duration-200 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                  >
                    {mode === "create" ? "Create" : "Save"}
                  </button>
                </div>
              </form>
            </div>
          </TransitionChild>
        </div>
      </Dialog>
    </Transition>
  );
};
