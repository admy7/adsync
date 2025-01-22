import React from "react";
import { CartesianGrid, Legend, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import { eachDayOfInterval, format, parseISO } from "date-fns";

import { CampaignViewModel as Campaign } from "../../api/contract.ts";

type CampaignTimelineChartProps = {
  campaigns: Campaign[]
};

const LINE_COLORS = ["#4f46e5",
  "#06b6d4",
  "#10b981",
  "#f59e0b",
  "#ef4444",
  "#8b5cf6",
  "#ec4899",
  "#f97316",
];


export const CampaignTimelineChart: React.FC<CampaignTimelineChartProps> = ({ campaigns }) => {
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

      // Calculate budget for each campaign and total budget
      campaigns.forEach(campaign => {
        const campaignStart = parseISO(campaign.start);
        const campaignEnd = parseISO(campaign.end);
        //
        if (date >= campaignStart && date <= campaignEnd) {
          dataPoint[campaign.name] = campaign.budget;
          dataPoint.total += campaign.budget;
        } else {
          dataPoint[campaign.name] = 0;
        }
      });

      return dataPoint;
    });
  }, [campaigns]);

  const campaignColors: Record<string, string> = React.useMemo(() => {

    return campaigns.reduce((acc, campaign, index) => ({
      ...acc,
      [campaign.name]: LINE_COLORS[index % LINE_COLORS.length],
    }), {});
  }, [campaigns]);

  return <div className="bg-white p-6 rounded-lg shadow">
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
  </div>;
};
