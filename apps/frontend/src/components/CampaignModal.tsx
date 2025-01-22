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
import { Channel } from "../api/contract.ts";
import { CampaignFormData } from "./Dashboard.tsx";
import { format } from "date-fns";

type CampaignModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: CampaignFormData) => void;
  initialData?: CampaignFormData;
  mode: "create" | "edit";
}

const channels: Channel[] = ["Radio", "TV", "Social media", "Search engine"];

export const CampaignModal: React.FC<CampaignModalProps> = ({
                                                              isOpen,
                                                              onClose,
                                                              onSubmit,
                                                              initialData,
                                                              mode,
                                                            }) => {

  const [formData, setFormData] = React.useState<CampaignFormData>(
    initialData || {
      name: "",
      channel: "Radio",
      budget: 0,
      start: "",
      end: "",
    },
  );

  useEffect(() => {
    if (isOpen) {
      setFormData(
        initialData || {
          name: "",
          channel: "Radio",
          budget: 0,
          start: "",
          end: "",
        }
      );
    }
  }, [isOpen, initialData]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
    onClose();
  };

  const inputClassName = "focus:ring-indigo-500 focus:ring-opacity-50 focus:border-indigo-500 focus:ring-2 focus:ring-opacity 5 focus:outline-none mt-1 block w-full px-4 py-3 text-base rounded-lg border-gray-300 border transition-colors duration-200";

  return (
    <Transition show={isOpen} as={React.Fragment}>
      <Dialog
        as="div"
        className="fixed inset-0 z-10 overflow-y-auto"
        onClose={onClose}
      >
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

          <span
            className="inline-block h-screen align-middle"
            aria-hidden="true"
          >
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
            <div
              className="inline-block w-full max-w-md p-8 my-8 overflow-hidden text-left align-middle transition-all transform bg-white shadow-xl rounded-2xl">
              <DialogTitle
                as="h3"
                className="text-xl font-semibold leading-6 text-gray-900"
              >
                {mode === "create" ? "Create Campaign" : "Edit Campaign"}
              </DialogTitle>

              <form onSubmit={handleSubmit} className="mt-6">
                <div className="space-y-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Campaign Name
                    </label>
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
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Channel
                    </label>
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
                        <ListboxButton
                          className="relative w-full cursor-pointer rounded-lg bg-white py-3 pl-4 pr-10 text-left text-base border border-gray-300 focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500 focus:ring-opacity-50 transition-colors duration-200">
                          <span className="block truncate">{formData.channel}</span>
                          <span className="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-3">
                            <ChevronDown
                              className="h-5 w-5 text-gray-400"
                              aria-hidden="true"
                            />
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
                            {channels.map((channel) => (
                              <ListboxOption
                                key={channel}
                                className={({ focus }) =>
                                  `relative cursor-pointer select-none py-3 pl-10 pr-4 ${
                                    focus
                                      ? "bg-indigo-50 text-indigo-900"
                                      : "text-gray-900"
                                  }`
                                }
                                value={channel}
                              >
                                {({ selected }) => (
                                  <>
                                    <span
                                      className={`block truncate ${
                                        selected ? "font-medium" : "font-normal"
                                      }`}
                                    >
                                      {channel}
                                    </span>
                                    {selected ? (
                                      <span
                                        className="absolute inset-y-0 left-0 flex items-center pl-3 text-indigo-600">
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
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Budget (€)
                    </label>
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
                      placeholder="Enter budget amount"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Start Date
                    </label>
                    <input
                      type="date"
                      className={inputClassName}
                      value={format(formData.start, "yyyy-MM-dd")}
                      onChange={(e) =>
                        setFormData({
                          ...formData,
                          start: e.target.value
                        })
                      }
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      End Date
                    </label>
                    <input
                      type="date"
                      className={inputClassName}
                      value={format(formData.end, "yyyy-MM-dd")}
                      onChange={(e) =>
                        setFormData({
                          ...formData,
                          end: e.target.value,
                        })
                      }
                    />
                  </div>
                </div>

                <div className="mt-8 flex justify-end space-x-3">
                  <button
                    type="button"
                    className="px-5 py-3 text-sm font-medium text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 transition-colors duration-200"
                    onClick={onClose}
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-5 py-3 text-sm font-medium text-white bg-indigo-600 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors duration-200"
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
