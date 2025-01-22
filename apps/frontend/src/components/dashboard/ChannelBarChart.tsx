import React from "react";
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";

import { CampaignViewModel as Campaign } from "../../api/contract.ts";

type CampaignBarChartProps = {
  campaigns: Campaign[]
};

type ChannelData = {
  channel: string;
  budget: number;
}

export const ChannelBarChart: React.FC<CampaignBarChartProps> = ({ campaigns }) => {
  const channelData = campaigns.reduce((acc: ChannelData[], campaign) => {
    const channel = acc.find((item) => item.channel === campaign.channel);
    if (channel) {
      channel.budget += campaign.budget;
    } else {
      acc.push({ channel: campaign.channel, budget: campaign.budget });
    }
    return acc;
  }, []);

  return <div className="bg-white p-6 rounded-lg shadow">
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
  </div>;
};
