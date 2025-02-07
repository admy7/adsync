import React from "react";
import { BarChart2, User, LogOut } from "lucide-react";
import { Menu, MenuButton, MenuItem, MenuItems, Transition } from "@headlessui/react";
import { useAuth } from "../providers/AuthProvider.tsx";

export const Navbar: React.FC = () => {
  const { logout } = useAuth();
  return (
    <nav className="bg-white shadow-sm">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div className="flex h-16 justify-between">
          <div className="flex items-center">
            <BarChart2 className="h-8 w-8 text-indigo-600" />
            <span className="ml-2 text-xl font-semibold text-gray-800">AdSync</span>
          </div>
          <div className="flex items-center">
            <Menu as="div" className="relative">
              <MenuButton className="flex h-10 w-10 items-center justify-center rounded-full bg-indigo-100 hover:bg-indigo-200 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2">
                <User className="h-5 w-5 text-indigo-600" />
              </MenuButton>
              <Transition
                as={React.Fragment}
                enter="transition ease-out duration-100"
                enterFrom="transform opacity-0 scale-95"
                enterTo="transform opacity-100 scale-100"
                leave="transition ease-in duration-75"
                leaveFrom="transform opacity-100 scale-100"
                leaveTo="transform opacity-0 scale-95"
              >
                <MenuItems className="absolute right-0 mt-2 w-48 origin-top-right rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
                  <div className="py-1">
                    <MenuItem>
                      {({ focus }) => (
                        <button
                          className={`${
                            focus ? "bg-gray-100" : ""
                          } flex w-full items-center px-4 py-2 text-sm text-gray-700`}
                          onClick={() => {
                            logout();
                          }}
                        >
                          <LogOut className="mr-2 h-4 w-4" />
                          Log out
                        </button>
                      )}
                    </MenuItem>
                  </div>
                </MenuItems>
              </Transition>
            </Menu>
          </div>
        </div>
      </div>
    </nav>
  );
};
