import React from "react";
import { CampaignViewModel as Campaign } from "../../api/contract.ts";

type CampaignKeyFiguresProps = {
  campaigns: Campaign[];
};

export const CampaignKeyFigures: React.FC<CampaignKeyFiguresProps> = ({ campaigns }) => {
  const totalBudget = campaigns.reduce((sum, campaign) => sum + campaign.budget, 0);

  return (
    <div className="mb-8 grid grid-cols-1 gap-6 md:grid-cols-3">
      <div className="rounded-lg bg-white p-6 shadow">
        <h3 className="mb-2 text-lg font-medium text-gray-900">Total Campaigns</h3>
        <p className="text-3xl font-bold text-indigo-600">{campaigns.length}</p>
      </div>
      <div className="rounded-lg bg-white p-6 shadow">
        <h3 className="mb-2 text-lg font-medium text-gray-900">Total Budget</h3>
        <p className="text-3xl font-bold text-indigo-600">{totalBudget.toLocaleString()}€</p>
      </div>
      <div className="rounded-lg bg-white p-6 shadow">
        <h3 className="mb-2 text-lg font-medium text-gray-900">Active Campaigns</h3>
        <p className="text-3xl font-bold text-indigo-600">
          {campaigns.filter((campaign) => new Date(campaign.end) >= new Date()).length}
        </p>
      </div>
    </div>
  );
};
