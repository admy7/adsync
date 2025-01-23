import React from "react";

export const Footer: React.FC = () => {
  return (
    <footer className="mt-auto bg-white">
      <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 gap-8 md:grid-cols-4">
          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wider text-gray-400">About Us</h3>
            <ul className="mt-4 space-y-4">
              <li>
                <a href="" className="text-base text-gray-500 hover:text-gray-900">
                  Company
                </a>
              </li>
              <li>
                <a href="" className="text-base text-gray-500 hover:text-gray-900">
                  Careers
                </a>
              </li>
            </ul>
          </div>
          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wider text-gray-400">Resources</h3>
            <ul className="mt-4 space-y-4">
              <li>
                <a href="" className="text-base text-gray-500 hover:text-gray-900">
                  Documentation
                </a>
              </li>
              <li>
                <a href="" className="text-base text-gray-500 hover:text-gray-900">
                  Help Center
                </a>
              </li>
            </ul>
          </div>
          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wider text-gray-400">Legal</h3>
            <ul className="mt-4 space-y-4">
              <li>
                <a href="" className="text-base text-gray-500 hover:text-gray-900">
                  Privacy
                </a>
              </li>
              <li>
                <a href="" className="text-base text-gray-500 hover:text-gray-900">
                  Terms
                </a>
              </li>
            </ul>
          </div>
          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wider text-gray-400">Contact</h3>
            <ul className="mt-4 space-y-4">
              <li>
                <a href="" className="text-base text-gray-500 hover:text-gray-900">
                  Contact Us
                </a>
              </li>
              <li>
                <a href="" className="text-base text-gray-500 hover:text-gray-900">
                  Support
                </a>
              </li>
            </ul>
          </div>
        </div>
        <div className="mt-8 border-t border-gray-200 pt-8">
          <p className="text-center text-base text-gray-400">
            © {new Date().getFullYear()} AdSync. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
};
