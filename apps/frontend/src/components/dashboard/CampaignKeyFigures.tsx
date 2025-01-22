import React from "react";
import { CampaignViewModel as Campaign } from "../../api/contract.ts";

type CampaignKeyFiguresProps = {
  campaigns: Campaign[];
}

export const CampaignKeyFigures: React.FC<CampaignKeyFiguresProps> = ({campaigns}) => {
  const totalBudget = campaigns.reduce((sum, campaign) => sum + campaign.budget, 0);

  return <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
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
        {totalBudget.toLocaleString()}€
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
  </div>;
};
